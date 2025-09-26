package com.fitzealot.dietservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DietPlanResponse(
        UUID id,
        String username,
        LocalDate generationDate,
        String fitnessGoal,
        String activityLevel,
        Integer mealsPerDay,
        List<String> dietaryPreferences,
        List<String> allergies,
        List<DailyDietPlanResponse> weeklyPlan,
        String status
) {}