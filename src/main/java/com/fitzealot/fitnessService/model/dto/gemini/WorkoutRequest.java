package com.fitzealot.fitnessService.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkoutRequest(
        String userId,
        double heightCm,
        double weightKg,
        String fitnessGoal,
        String activityLevel,
        String preferredWorkoutTime,
        int workoutDaysPerWeek,
        String description
) {
}
