package com.fitzealot.dietservice.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DietPlanRequest(
        String userId,
        double heightCm,
        double weightKg,
        String fitnessGoal,
        String activityLevel,
        Integer mealsPerDay,
        List<String> dietaryPreferences,
        List<String> allergies,
        String description
) {
    // You can still add custom constructors or methods if needed,
    // but without validation annotations.
}