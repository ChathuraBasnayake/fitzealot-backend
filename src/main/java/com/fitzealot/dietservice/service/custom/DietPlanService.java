package com.fitzealot.dietservice.service.custom;

import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.dto.response.DietPlanResponse;
import jakarta.validation.Valid;

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
     * Retrieves the latest diet plan for a specific username.
     *
     * @param username The username to search by.
     * @return A DietPlanResponse object or null if not found.
     */
    DietPlanResponse getDietPlanByUsername(String username);

    DietPlanResponse update(@Valid DietPlanRequest dietPlanRequest);
}
