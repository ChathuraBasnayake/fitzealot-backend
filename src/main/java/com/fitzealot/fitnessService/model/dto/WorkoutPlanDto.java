package com.fitzealot.fitnessService.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutPlanDto {

    private String username;

    private int durationWeeks;

    private LocalDateTime createdAt;

    private List<DailyWorkoutDto> weeklyPlan = new ArrayList<>();

}
