package com.fitzealot.fitnessService.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiDailyPlanDto(
        String dayOfWeek,
        boolean isRestDay,
        String workoutType,
        List<String> warmUp,
        List<GeminiExerciseDto> exercises,
        List<String> coolDown
) {
}
