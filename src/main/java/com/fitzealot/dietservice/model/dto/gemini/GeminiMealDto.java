package com.fitzealot.dietservice.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiMealDto(
        @JsonProperty("mealType") String mealType,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("ingredients") List<String> ingredients,
        @JsonProperty("preparationInstructions") List<String> preparationInstructions,
        @JsonProperty("approxCalories") Double approxCalories,
        @JsonProperty("approxMacros") GeminiMacrosDto approxMacros
) {}
