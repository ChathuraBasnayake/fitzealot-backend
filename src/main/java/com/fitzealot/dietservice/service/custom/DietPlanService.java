package com.fitzealot.dietservice.service.custom;

import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.dto.response.DietPlanResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DietPlanService {
    /**
     * Synchronously creates a new personalized diet plan using AI and saves it.
     *
     * @param dietPlanRequest The request containing user preferences.
     * @return The newly created DietPlanResponse DTO.
     */
    DietPlanResponse createDietPlan(DietPlanRequest dietPlanRequest);

    /**
     * Retrieves a diet plan by its ID, mapped to a DietPlanResponse DTO.
     *
     * @param id The UUID of the diet plan.
     * @return An Optional containing the DietPlanResponse if found, otherwise empty.
     */
    Optional<DietPlanResponse> getDietPlanById(UUID id);

    /**
     * Retrieves all diet plans for a specific user ID, mapped to DietPlanResponse DTOs.
     *
     * @param userId The ID of the user.
     * @return A list of DietPlanResponse objects.
     */
    List<DietPlanResponse> getDietPlansByUserId(String userId);
}
