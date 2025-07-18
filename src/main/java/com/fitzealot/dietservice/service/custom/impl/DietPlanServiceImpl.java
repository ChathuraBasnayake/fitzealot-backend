package com.fitzealot.dietservice.service.custom.impl;

import com.fitzealot.dietservice.exception.ServiceException; // Still needed if you want post-generation events
import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.dto.response.DailyDietPlanResponse;
import com.fitzealot.dietservice.model.dto.response.DietPlanResponse;
import com.fitzealot.dietservice.model.dto.response.MacrosResponse;
import com.fitzealot.dietservice.model.dto.response.MealResponse;
import com.fitzealot.dietservice.model.entity.DailyDietPlan;
import com.fitzealot.dietservice.model.entity.DietPlan;
import com.fitzealot.dietservice.model.entity.Meal;
import com.fitzealot.dietservice.repository.DietPlanRepository;
import com.fitzealot.dietservice.service.custom.DietPlanService;
import com.fitzealot.dietservice.service.custom.impl.GeminiService;
import com.fitzealot.dietservice.service.event.DietPlanEventsPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class DietPlanServiceImpl implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final GeminiService geminiService;
    private final DietPlanEventsPublisher eventsPublisher; // Keep if you want post-generation events, remove otherwise

    /**
     * Synchronously creates a diet plan and returns its DTO representation.
     * This method directly calls the generation logic in GeminiService,
     * then maps the returned entity to a DTO.
     *
     * @param dietPlanRequest The request containing user preferences.
     * @return A DietPlanResponse DTO of the newly created plan.
     */
    @Override
    @Transactional // Ensure this operation is atomic: generate + save
    public DietPlanResponse createDietPlan(DietPlanRequest dietPlanRequest) {
        log.info("Creating diet plan for user: {}", dietPlanRequest.userId());
        try {
            // 1. Call GeminiService to generate and save the DietPlan entity
            //    GeminiService now returns the DietPlan entity.
            DietPlan generatedPlanEntity = geminiService.generateAndSaveDietPlan(dietPlanRequest);

            // 2. Map the DietPlan entity to a DietPlanResponse DTO
            DietPlanResponse dietPlanResponse = mapToDietPlanResponse(generatedPlanEntity);

            // Optional: Publish a "plan generated" event for audit/notifications
            // This event is *not* for triggering generation, but for notifying about completion.
            eventsPublisher.publishDietPlanGeneratedEvent(dietPlanResponse.userId(), dietPlanResponse.id(), "SUCCESS_SYNCHRONOUS");

            return dietPlanResponse;
        } catch (ServiceException e) {
            log.error("Service error during diet plan creation for user {}: {}", dietPlanRequest.userId(), e.getMessage());
            eventsPublisher.publishDietPlanGenerationFailedEvent(dietPlanRequest.userId(), "Generation failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during diet plan creation for user {}: {}", dietPlanRequest.userId(), e.getMessage(), e);
            eventsPublisher.publishDietPlanGenerationFailedEvent(dietPlanRequest.userId(), "Unexpected error during generation: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred during diet plan creation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DietPlanResponse> getDietPlanById(UUID id) {
        return dietPlanRepository.findById(id)
                .map(this::mapToDietPlanResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietPlanResponse> getDietPlansByUserId(String userId) {

        return  null;
//        List<DietPlan> dietPlans = dietPlanRepository.findByUserId(userId);
//        return dietPlans.stream()
//                .map(this::mapToDietPlanResponse)
//                .collect(Collectors.toList());
    }

    // --- Private Mapping Methods (from Entity to Response DTO) ---
    // These methods are correctly placed here in the DietPlanServiceImpl.

    private DietPlanResponse mapToDietPlanResponse(DietPlan entity) {
        if (entity == null) {
            return null;
        }

        List<DailyDietPlanResponse> dailyPlanResponses = entity.getWeeklyPlan().stream()
                .map(this::mapToDailyDietPlanResponse)
                .collect(Collectors.toList());

        return new DietPlanResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getGenerationDate(),
                entity.getFitnessGoal(),
                entity.getActivityLevel(),
                entity.getMealsPerDay(),
                entity.getDietaryPreferences(),
                entity.getAllergies(),
                dailyPlanResponses,
                entity.getStatus().name()
        );
    }

    private DailyDietPlanResponse mapToDailyDietPlanResponse(DailyDietPlan entity) {
        if (entity == null) {
            return null;
        }

        List<MealResponse> mealResponses = entity.getMealPlan().stream()
                .map(this::mapToMealResponse)
                .collect(Collectors.toList());

        return new DailyDietPlanResponse(
                entity.getId(),
                entity.getDayOfWeek(),
                entity.isRestDay(),
                mealResponses,
                entity.getNotes()
        );
    }

    private MealResponse mapToMealResponse(Meal entity) {
        if (entity == null) {
            return null;
        }

        MacrosResponse macrosResponse = null;
        if (entity.getMacros() != null) {
            macrosResponse = new MacrosResponse(
                    entity.getMacros().getProtein(),
                    entity.getMacros().getCarbs(),
                    entity.getMacros().getFat()
            );
        }

        return new MealResponse(
                entity.getId(),
                entity.getMealType(),
                entity.getName(),
                entity.getDescription(),
                entity.getIngredients(),
                entity.getPreparationInstructions(),
                entity.getApproxCalories(),
                macrosResponse
        );
    }
}