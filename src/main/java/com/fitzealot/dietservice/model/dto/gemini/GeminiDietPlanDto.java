package com.fitzealot.dietservice.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiDietPlanDto(
        @JsonProperty("weeklyDietPlan") List<GeminiDailyDietPlanDto> weeklyDietPlan
) {}
