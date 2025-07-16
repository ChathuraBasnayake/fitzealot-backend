package com.fitzealot.fitnessService.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiWorkoutPlanDto(List<GeminiDailyPlanDto> weeklyPlan) {
}
