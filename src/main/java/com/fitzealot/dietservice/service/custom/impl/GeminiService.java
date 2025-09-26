package com.fitzealot.dietservice.service.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fitzealot.dietservice.exception.ServiceException;
import com.fitzealot.dietservice.model.dto.gemini.GeminiDailyDietPlanDto;
import com.fitzealot.dietservice.model.dto.gemini.GeminiDietPlanDto;
import com.fitzealot.dietservice.model.dto.gemini.GeminiMacrosDto;
import com.fitzealot.dietservice.model.dto.gemini.GeminiMealDto;
import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.entity.DailyDietPlan;
import com.fitzealot.dietservice.model.entity.DietPlan;
import com.fitzealot.dietservice.util.DietPlanStatus;
import com.fitzealot.dietservice.model.entity.Macros;
import com.fitzealot.dietservice.model.entity.Meal;
import com.fitzealot.dietservice.repository.DietPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GeminiService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;
    private final DietPlanRepository dietPlanRepository;
    private final ObjectMapper objectMapper;

    public GeminiService(@Value("${gemini.api.key}") String apiKey,
                         @Value("${gemini.api.url}") String apiUrl,
                         DietPlanRepository dietPlanRepository,
                         ObjectMapper objectMapper,
                         RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.dietPlanRepository = dietPlanRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public DietPlan generateAndSaveDietPlan(DietPlanRequest request) {
        try {
            // 1. Create prompt for Gemini
            String prompt = createPrompt(request);
            String requestBody = createGeminiRequestBody(prompt);
            String urlWithKey = apiUrl + "?key=" + apiKey;

            // 2. Call Gemini API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(urlWithKey, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Gemini API call failed with status: {} and body: {}", response.getStatusCode(), response.getBody());
                throw new ServiceException("Gemini API call failed with status: " + response.getStatusCode());
            }

            // 3. Extract and parse response
            String jsonBody = extractContentFromJson(response.getBody());
            GeminiDietPlanDto dto = parseJsonToDto(jsonBody);

            // 4. Map DTO to Entity and save
            DietPlan plan = mapDtoToEntity(dto, request.username(), request);
            DietPlan existingPlan = dietPlanRepository.findByUsername(request.username());

            if (existingPlan != null) {
                plan.setId(existingPlan.getId());
            }

            return dietPlanRepository.save(plan);


        } catch (RestClientException e) {
            log.error("API call to Gemini failed for diet plan generation", e);
            throw new ServiceException("Failed to communicate with Gemini API for diet plan", e);
        } catch (ServiceException e) {
            // Re-throw custom ServiceExceptions
            throw e;
        } catch (Exception e) {
            log.error("Error generating diet plan", e);
            throw new ServiceException("Failed to generate diet plan", e);
        }
    }

    /**
     * Creates the prompt string for the Gemini AI.
     * Specifies the user profile and the required JSON output format.
     */
    private String createPrompt(DietPlanRequest request) {
        // Build dietary preferences and allergies string
        String dietaryPrefs = request.dietaryPreferences() != null && !request.dietaryPreferences().isEmpty()
                ? String.join(", ", request.dietaryPreferences())
                : "None";
        String allergies = request.allergies() != null && !request.allergies().isEmpty()
                ? String.join(", ", request.allergies())
                : "None";

        return String.format("""
                        You are an expert nutritionist and diet planner AI. Your task is to create a detailed, personalized weekly diet plan based on the user's profile and preferences.
                        
                        **User Profile:**
                        - Height: %.1f cm
                        - Weight: %.1f kg
                        - Primary Fitness Goal: "%s"
                        - Current Activity Level: "%s"
                        - Meals Per Day: %d
                        - Dietary Preferences: %s
                        - Allergies: %s
                        - Description: "%s"
                        
                        **Instructions:**
                        1. Create a balanced diet plan for a full 7-day week.
                        2. Ensure the plan aligns with the user's "Primary Fitness Goal" (e.g., calorie surplus for muscle gain, deficit for weight loss).
                        3. The number of meals per day should strictly adhere to the "Meals Per Day" specified by the user.
                        4. Incorporate "Dietary Preferences" and avoid "Allergies". If a preference/allergy makes a plan impossible or very restrictive, provide the best possible alternative or a note about it.
                        5. For each day, include meals for "Breakfast", "Lunch", "Dinner", and any specified snacks (if "Meals Per Day" allows).
                        6. For each meal, provide:
                            - "mealType" (e.g., "Breakfast", "Lunch", "Dinner", "Snack 1")
                            - "name" of the dish (e.g., "Grilled Chicken Salad")
                            - "description" (a brief overview of the meal)
                            - "ingredients" (a list of main ingredients with approximate quantities)
                            - "preparationInstructions" (brief, actionable steps to prepare)
                            - "approxCalories" (estimated total calories for that meal)
                            - "approxMacros" (estimated protein, carbs, fat in grams, e.g., "Protein: 30g, Carbs: 40g, Fat: 15g")
                        7. For any rest days (which you can infer based on the plan's overall balance or if a specific prompt indicated it, though for diet it's less common), you can mark 'isRestDay' as true and suggest slightly lighter options or focus on recovery nutrition. If no specific rest day is implied, 'isRestDay' should be false.
                        8. Include "notes" for each day, providing general tips or advice for that day's nutrition.
                        
                        **Output Format:**
                        Respond ONLY with a valid JSON object. Do not include any text, markdown, or explanations outside of the JSON. The structure must follow this exact schema:
                        {
                          "weeklyDietPlan": [
                            {
                              "dayOfWeek": "Monday",
                              "isRestDay": boolean, // true if it's a lighter intake day / flexible day, false otherwise
                              "mealPlan": [
                                {
                                  "mealType": "string",
                                  "name": "string",
                                  "description": "string",
                                  "ingredients": ["string", ...],
                                  "preparationInstructions": ["string", ...],
                                  "approxCalories": double,
                                  "approxMacros": { "protein": "string", "carbs": "string", "fat": "string" }
                                },
                                ...
                              ],
                              "notes": "string"
                            },
                            ...
                          ]
                        }
                        """,
                request.heightCm(),
                request.weightKg(),
                request.fitnessGoal(),
                request.activityLevel(),
                request.mealsPerDay(),
                dietaryPrefs,
                allergies,
                request.description().replace("\"", "\\\"") // Escape quotes in description for JSON safety
        );
    }

    /**
     * Creates the JSON request body for the Gemini API call.
     */
    private String createGeminiRequestBody(String prompt) {
        return String.format("""
                        {
                            "contents": [{
                                "parts": [{
                                    "text": "%s"
                                }]
                            }]
                        }""",
                prompt.replace("\"", "\\\"") // Escape quotes in the prompt
        );
    }

    /**
     * Extracts the relevant JSON content from Gemini's full API response.
     * Handles cases where Gemini wraps the JSON in markdown code blocks.
     */
    private String extractContentFromJson(String apiResponse) {
        try {
            if (apiResponse == null || apiResponse.isEmpty()) {
                throw new ServiceException("Empty or null response received from Gemini API");
            }

            ObjectNode root = (ObjectNode) objectMapper.readTree(apiResponse);

            // Navigate to the 'text' field which contains the generated content
            String content = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            if (content.isEmpty()) {
                // Check if content might be in an 'error' field or similar for specific API error responses
                if (root.has("error")) {
                    String errorMessage = root.path("error").path("message").asText("Unknown Gemini API error");
                    throw new ServiceException("Gemini API returned an error: " + errorMessage);
                }
                throw new ServiceException("No content found in Gemini response 'text' field. Response: " + apiResponse);
            }

            if (content.startsWith("```json") && content.endsWith("```")) {
                content = content.substring("```json".length(), content.length() - "```".length()).trim();
            } else if (content.startsWith("```") && content.endsWith("```")) { // Generic markdown block
                content = content.substring("```".length(), content.length() - "```".length()).trim();
            }

            return content;
        } catch (ServiceException e) {
            throw e; // Re-throw custom service exceptions
        } catch (Exception e) {
            log.error("Error extracting content from Gemini API response: {}", apiResponse, e);
            throw new ServiceException("Error processing Gemini API response content", e);
        }
    }

    /**
     * Parses the clean JSON string into a GeminiDietPlanDto record.
     */
    private GeminiDietPlanDto parseJsonToDto(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, GeminiDietPlanDto.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON response into GeminiDietPlanDto: {}", jsonContent, e);
            throw new ServiceException("Failed to parse diet plan JSON content from Gemini", e);
        }
    }

    /**
     * Maps the parsed GeminiDietPlanDto to your DietPlan entity structure.
     */
    private DietPlan mapDtoToEntity(GeminiDietPlanDto geminiDto, String userId, DietPlanRequest originalRequest) {
        if (geminiDto == null || geminiDto.weeklyDietPlan() == null) {
            throw new IllegalArgumentException("Invalid Gemini diet plan DTO for mapping.");
        }

        DietPlan dietPlan = new DietPlan();
        dietPlan.setUsername(userId);
        // Copy relevant details from the original request
        dietPlan.setFitnessGoal(originalRequest.fitnessGoal());
        dietPlan.setActivityLevel(originalRequest.activityLevel());
        dietPlan.setMealsPerDay(originalRequest.mealsPerDay());
        dietPlan.setDietaryPreferences(originalRequest.dietaryPreferences());
        dietPlan.setAllergies(originalRequest.allergies());
        dietPlan.setDescription(originalRequest.description());
        dietPlan.setStatus(DietPlanStatus.GENERATED); // Set status upon successful generation

        List<DailyDietPlan> dailyPlans = geminiDto.weeklyDietPlan().stream()
                .map(this::mapDailyDietDtoToEntity)
                .collect(Collectors.toList());

        // Establish bidirectional relationship
        dailyPlans.forEach(dietPlan::addDailyDietPlan);

        return dietPlan;
    }

    /**
     * Maps a GeminiDailyDietPlanDto record to a DailyDietPlan entity.
     */
    private DailyDietPlan mapDailyDietDtoToEntity(GeminiDailyDietPlanDto dailyDto) {
        if (dailyDto == null) {
            throw new IllegalArgumentException("Daily diet plan DTO cannot be null.");
        }

        DailyDietPlan dailyDietPlan = new DailyDietPlan();
        dailyDietPlan.setDayOfWeek(dailyDto.dayOfWeek());
        dailyDietPlan.setRestDay(dailyDto.isRestDay() != null ? dailyDto.isRestDay() : false); // Default to false if null
        dailyDietPlan.setNotes(dailyDto.notes());

        if (dailyDto.mealPlan() != null) {
            List<Meal> meals = dailyDto.mealPlan().stream()
                    .map(this::mapMealDtoToEntity)
                    .collect(Collectors.toList());
            meals.forEach(dailyDietPlan::addMeal); // Establish bidirectional relationship
        }

        return dailyDietPlan;
    }

    /**
     * Maps a GeminiMealDto record to a Meal entity.
     */
    private Meal mapMealDtoToEntity(GeminiMealDto mealDto) {
        if (mealDto == null) {
            throw new IllegalArgumentException("Meal DTO cannot be null.");
        }

        Meal meal = new Meal();
        meal.setMealType(mealDto.mealType());
        meal.setName(mealDto.name());
        meal.setDescription(mealDto.description());
        meal.setIngredients(mealDto.ingredients());
        meal.setPreparationInstructions(mealDto.preparationInstructions());
        meal.setApproxCalories(mealDto.approxCalories());

        if (mealDto.approxMacros() != null) {
            Macros macros = mapMacrosDtoToEntity(mealDto.approxMacros());
            meal.setMacros(macros);
        }

        return meal;
    }

    /**
     * Maps a GeminiMacrosDto record to a Macros embeddable.
     */
    private Macros mapMacrosDtoToEntity(GeminiMacrosDto macrosDto) {
        if (macrosDto == null) {
            return null; // Or throw an exception if macros are always expected
        }
        return new Macros(macrosDto.protein(), macrosDto.carbs(), macrosDto.fat());
    }
}