package com.fitzealot.fitnessService.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkoutRequest(
        double heightCm,
        double weightKg,
        String fitnessGoal,
        String activityLevel,
        String preferredWorkoutTime,
        int workoutDaysPerWeek,
        String description
) {}
