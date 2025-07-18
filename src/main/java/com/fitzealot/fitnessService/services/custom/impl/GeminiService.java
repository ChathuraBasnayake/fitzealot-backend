package com.fitzealot.fitnessService.services.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fitzealot.fitnessService.exception.ServiceException;
import com.fitzealot.fitnessService.model.dto.gemini.WorkoutRequest;
import com.fitzealot.fitnessService.model.dto.gemini.GeminiDailyPlanDto;
import com.fitzealot.fitnessService.model.dto.gemini.GeminiExerciseDto;
import com.fitzealot.fitnessService.model.dto.gemini.GeminiWorkoutPlanDto;
import com.fitzealot.fitnessService.model.entity.DailyWorkout;
import com.fitzealot.fitnessService.model.entity.ExerciseDetail;
import com.fitzealot.fitnessService.model.entity.WorkoutPlan;
import com.fitzealot.fitnessService.repository.WorkoutPlanRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@Validated
public class GeminiService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ObjectMapper objectMapper;

    public GeminiService(@Value("${gemini.api.key}") String apiKey,
                         @Value("${gemini.api.url}") String apiUrl,
                         WorkoutPlanRepository workoutPlanRepository,
                         ObjectMapper objectMapper,
                         RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.workoutPlanRepository = workoutPlanRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public WorkoutPlan generateAndSaveWorkoutPlan(@Valid WorkoutRequest request) {
        try {
            String prompt = createPrompt(request);
            String requestBody = createGeminiRequestBody(prompt);
            String urlWithKey = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(urlWithKey, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("Gemini API call failed with status: " + response.getStatusCode());
            }

            String jsonBody = extractContentFromJson(response.getBody());
            GeminiWorkoutPlanDto dto = parseJsonToDto(jsonBody);
            WorkoutPlan plan = mapDtoToEntity(dto);

            return workoutPlanRepository.save(plan);
        } catch (RestClientException e) {
            log.error("API call to Gemini failed", e);
            throw new ServiceException("Failed to communicate with Gemini API", e);
        } catch (Exception e) {
            log.error("Error generating workout plan", e);
            throw new ServiceException("Failed to generate workout plan", e);
        }
    }

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
                1. Create a plan for a full 7-day week.
                2. The number of workout days must exactly match the user's request. The remaining days are rest days.
                3. Logically distribute the workout and rest days.
                4. For each workout day, provide a clear "workoutType" (e.g., "Upper Body Strength", "Cardio & Core").
                5. Include a brief "warmUp" routine with 2-3 simple exercises.
                6. List 4-6 "exercises", each with a specified number of "sets" and a "reps" range (e.g., "8-12 reps") or duration.
                7. Include a "coolDown" routine with 2-3 static stretches.
                8. For rest days, set "isRestDay" to true and you can optionally suggest a light activity.
                
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

    private String createGeminiRequestBody(String prompt) {
        return String.format("""
                {
                    "contents": [{
                        "parts": [{
                            "text": "%s"
                        }]
                    }]
                }""",
                prompt.replace("\"", "\\\"")
        );
    }

    private GeminiWorkoutPlanDto parseJsonToDto(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, GeminiWorkoutPlanDto.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON response", e);
            throw new ServiceException("Failed to parse workout plan JSON content", e);
        }
    }

    private WorkoutPlan mapDtoToEntity(GeminiWorkoutPlanDto dto) {
        if (dto == null || dto.weeklyPlan() == null) {
            throw new IllegalArgumentException("Invalid workout plan data received");
        }

        WorkoutPlan plan = new WorkoutPlan();
        plan.setWeeklyPlan(dto.weeklyPlan().stream()
                .map(this::mapDailyDtoToEntity)
                .toList());
        return plan;
    }

    private DailyWorkout mapDailyDtoToEntity(GeminiDailyPlanDto dailyDto) {
        if (dailyDto == null) {
            throw new IllegalArgumentException("Daily workout data cannot be null");
        }

        DailyWorkout dailyWorkout = new DailyWorkout();
        dailyWorkout.setDayOfWeek(dailyDto.dayOfWeek());
        dailyWorkout.setRestDay(dailyDto.isRestDay());
        dailyWorkout.setWorkoutType(dailyDto.workoutType());

        if (dailyDto.warmUp() != null) {
            dailyWorkout.setWarmUp(dailyDto.warmUp());
        }
        if (dailyDto.coolDown() != null) {
            dailyWorkout.setCoolDown(dailyDto.coolDown());
        }

        if (dailyDto.exercises() != null) {
            List<ExerciseDetail> exercises = dailyDto.exercises().stream()
                    .map(this::mapExerciseDto)
                    .toList();
            dailyWorkout.setExercises(exercises);
        }

        return dailyWorkout;
    }

    private ExerciseDetail mapExerciseDto(GeminiExerciseDto exDto) {
        if (exDto == null) {
            throw new IllegalArgumentException("Exercise data cannot be null");
        }

        ExerciseDetail ex = new ExerciseDetail();
        ex.setName(exDto.name());
        ex.setSets(exDto.sets());
        ex.setReps(exDto.reps());
        return ex;
    }

    private String extractContentFromJson(String apiResponse) {
        try {
            if (apiResponse == null) {
                throw new ServiceException("Empty response received from Gemini API");
            }

            ObjectNode root = (ObjectNode) objectMapper.readTree(apiResponse);
            String content = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            if (content.isEmpty()) {
                throw new ServiceException("No content found in Gemini response");
            }

            // --- IMPORTANT CHANGE HERE ---
            // Remove markdown code block fences if they exist
            if (content.startsWith("```json") && content.endsWith("```")) {
                content = content.substring("```json".length(), content.length() - "```".length()).trim();
            } else if (content.startsWith("```") && content.endsWith("```")) { // Generic markdown block
                content = content.substring("```".length(), content.length() - "```".length()).trim();
            }
            // --- END IMPORTANT CHANGE ---

            return content;
        } catch (Exception e) {
            log.error("Error parsing API response", e);
            throw new ServiceException("Error parsing Gemini API response", e);
        }
    }
}