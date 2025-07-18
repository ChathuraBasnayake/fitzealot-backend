package com.fitzealot.dietservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DailyDietPlanResponse(
        UUID id,
        String dayOfWeek,
        boolean isRestDay,
        List<MealResponse> mealPlan,
        String notes
) {}