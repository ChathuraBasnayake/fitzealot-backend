package com.fitzealot.fitnessService.services.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.model.dto.gemini.GeminiDailyPlanDto;
import com.fitzealot.fitnessService.model.dto.gemini.GeminiWorkoutPlanDto;
import com.fitzealot.fitnessService.model.entity.DailyWorkout;
import com.fitzealot.fitnessService.model.entity.ExerciseDetail;
import com.fitzealot.fitnessService.model.entity.WorkoutPlan;
import com.fitzealot.fitnessService.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Service class responsible for interacting with the Gemini API
 * to generate and save workout plans.
 */
@Service
public class GeminiService {

    private final WebClient webClient;
    private final String apiKey;
    private final String apiUrl;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructor for dependency injection. Spring will automatically provide
     * the required beans (WebClient.Builder, ObjectMapper) and inject values
     * from application.properties.
     */
    public GeminiService(WebClient.Builder webClientBuilder,
                         @Value("${gemini.api.key}") String apiKey,
                         @Value("${gemini.api.url}") String apiUrl,
                         WorkoutPlanRepository workoutPlanRepository,
                         ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.workoutPlanRepository = workoutPlanRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Main method to generate a workout plan from a user request, call the Gemini API,
     * parse the response, and save it to the database.
     * The @Transactional annotation ensures that the entire process is a single database transaction.
     *
     * @param request The user's workout preferences.
     * @return A Mono containing the saved WorkoutPlan entity from the database.
     */
    @Transactional
    public Mono<WorkoutPlan> generateAndSaveWorkoutPlan(WorkoutRequest request) {
        String prompt = createPrompt(request);
        String requestBody = createGeminiRequestBody(prompt);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::extractContentFromJson)
                .doOnNext(jsonContent -> { // Log the raw response for debugging purposes
                    System.out.println("=========================================");
                    System.out.println("Raw JSON content from Gemini:");
                    System.out.println(jsonContent);
                    System.out.println("=========================================");
                })
                .map(this::parseJsonToDto)
                .map(this::mapDtoToEntity)
                .map(workoutPlanRepository::save);
    }

    /**
     * Parses the JSON string from Gemini into a GeminiWorkoutPlanDto object.
     *
     * @param jsonContent The JSON string part of the Gemini response.
     * @return A GeminiWorkoutPlanDto object.
     */
    private GeminiWorkoutPlanDto parseJsonToDto(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, GeminiWorkoutPlanDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse workout plan JSON content", e);
        }
    }

    /**
     * Maps the data from the DTO (Data Transfer Object) to a persistable WorkoutPlan entity.
     *
     * @param dto The DTO parsed from the Gemini response.
     * @return A WorkoutPlan entity ready to be saved.
     */
    private WorkoutPlan mapDtoToEntity(GeminiWorkoutPlanDto dto) {
        // Add a null check to prevent NullPointerException
        if (dto == null || dto.weeklyPlan() == null) {
            throw new IllegalStateException("The workout plan DTO or its weeklyPlan list is null. This indicates a problem with parsing the AI's response. Check the raw JSON log above.");
        }
        WorkoutPlan plan = new WorkoutPlan();
        plan.setWeeklyPlan(dto.weeklyPlan().stream()
                .map(this::mapDailyDtoToEntity)
                .collect(Collectors.toList()));
        return plan;
    }

    /**
     * Helper method to map a single day's DTO to a DailyWorkout entity.
     */
    private DailyWorkout mapDailyDtoToEntity(GeminiDailyPlanDto dailyDto) {
        DailyWorkout dailyWorkout = new DailyWorkout();
        dailyWorkout.setDayOfWeek(dailyDto.dayOfWeek());
        dailyWorkout.setRestDay(dailyDto.isRestDay());
        dailyWorkout.setWorkoutType(dailyDto.workoutType());
        if (dailyDto.warmUp() != null) dailyWorkout.getWarmUp().addAll(dailyDto.warmUp());
        if (dailyDto.coolDown() != null) dailyWorkout.getCoolDown().addAll(dailyDto.coolDown());
        if (dailyDto.exercises() != null) {
            dailyWorkout.setExercises(dailyDto.exercises().stream().map(exDto -> {
                ExerciseDetail ex = new ExerciseDetail();
                ex.setName(exDto.name());
                ex.setSets(exDto.sets());
                ex.setReps(exDto.reps());
                return ex;
            }).collect(Collectors.toList()));
        }
        return dailyWorkout;
    }

    /**
     * Extracts the core JSON content from the full Gemini API response shell.
     */
    private Mono<String> extractContentFromJson(String apiResponse) {
        try {
            ObjectNode root = (ObjectNode) objectMapper.readTree(apiResponse);
            String content = root.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
            if (content.isEmpty()) {
                System.err.println("Could not find content in Gemini response. Full response: " + apiResponse);
                return Mono.error(new RuntimeException("Failed to extract content from Gemini response."));
            }
            return Mono.just(content);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error parsing Gemini API response", e));
        }
    }

    /**
     * Creates the detailed prompt string to be sent to the Gemini API.
     */
    private String createPrompt(WorkoutRequest request) {
        return String.format("""
                            You are an expert personal trainer and fitness AI. Your task is to create a detailed, personalized weekly workout plan based on the user's profile.
                        
                            **User Profile:**
                            - Height: %.1f cm
                            - Weight: %.1f kg
                            - Primary Fitness Goal: "%s"
                            - Current Activity Level: "%s"
                            - Preferred Workout Time: %s
                            - Workout Days Per Week: %d
                            - description: "%s"
                        
                            **Instructions:**
                            1.  Create a plan for a full 7-day week.
                            2.  The number of workout days must exactly match the user's request. The remaining days are rest days.
                            3.  Logically distribute the workout and rest days.
                            4.  For each workout day, provide a clear "workoutType" (e.g., "Upper Body Strength", "Cardio & Core").
                            5.  Include a brief "warmUp" routine with 2-3 simple exercises.
                            6.  List 4-6 "exercises", each with a specified number of "sets" and a "reps" range (e.g., "8-12 reps") or duration.
                            7.  Include a "coolDown" routine with 2-3 static stretches.
                            8.  For rest days, set "isRestDay" to true and you can optionally suggest a light activity.
                        
                            **Output Format:**
                            Respond ONLY with a valid JSON object. Do not include any text, markdown, or explanations outside of the JSON. The structure must follow this exact schema:
                            {
                              "weeklyPlan": [
                                {
                                  "dayOfWeek": "Monday",
                                  "isRestDay": boolean,
                                  "workoutType": "string | null",
                                  "warmUp": ["string", ...],
                                  "exercises": [
                                    { "name": "string", "sets": number, "reps": "string" },
                                    ...
                                  ],
                                  "coolDown": ["string", ...]
                                },
                                ...
                              ]
                            }
                        """,
                request.heightCm(),
                request.weightKg(),
                request.fitnessGoal(),
                request.activityLevel(),
                request.preferredWorkoutTime(),
                request.workoutDaysPerWeek(),
                request.description());
    }

    /**
     * Creates the full JSON request body required by the Gemini API.
     */
    private String createGeminiRequestBody(String prompt) {
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode contents = root.putArray("contents");
        ObjectNode content = contents.addObject();
        content.put("role", "user");
        ArrayNode parts = content.putArray("parts");
        parts.addObject().put("text", prompt);
        ObjectNode generationConfig = root.putObject("generationConfig");
        generationConfig.put("responseMimeType", "application/json");
        try {
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException("Error creating request body JSON", e);
        }
    }
}
