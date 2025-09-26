package com.fitzealot.fitnessService.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkoutRequest(
        @NotBlank(message = "username is required")
        String username,

        @Positive(message = "heightCm must be positive")
        double heightCm,

        @Positive(message = "weightKg must be positive")
        double weightKg,

        @NotBlank(message = "fitnessGoal is required")
        String fitnessGoal,

        @NotBlank(message = "activityLevel is required")
        String activityLevel,

        @NotBlank(message = "preferredWorkoutTime is required")
        String preferredWorkoutTime,

        @Min(value = 1, message = "workoutDaysPerWeek must be at least 1")
        @Max(value = 7, message = "workoutDaysPerWeek cannot exceed 7")
        int workoutDaysPerWeek,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description
) {
}
