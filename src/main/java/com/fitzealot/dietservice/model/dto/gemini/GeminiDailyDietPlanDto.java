package com.fitzealot.dietservice.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiDailyDietPlanDto(
        @JsonProperty("dayOfWeek") String dayOfWeek,
        @JsonProperty("isRestDay") Boolean isRestDay,
        @JsonProperty("mealPlan") List<GeminiMealDto> mealPlan,
        @JsonProperty("notes") String notes
) {}
