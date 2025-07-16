package com.fitzealot.fitnessService.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutPlanDto {

    private Long id;

    // **NEW FIELD ADDED** to match the database table
    private int durationWeeks;

    private LocalDateTime createdAt;

    private List<DailyWorkoutDto> weeklyPlan = new ArrayList<>();

}
