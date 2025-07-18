package com.fitzealot.dietservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MealResponse(
        UUID id,
        String mealType,
        String name,
        String description,
        List<String> ingredients,
        List<String> preparationInstructions,
        Double approxCalories,
        MacrosResponse macros
) {}