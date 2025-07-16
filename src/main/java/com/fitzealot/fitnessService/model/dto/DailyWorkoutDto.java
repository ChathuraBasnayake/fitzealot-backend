package com.fitzealot.fitnessService.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DailyWorkoutDto {

    private Long id;

    private String dayOfWeek;
    private boolean isRestDay;
    private String workoutType;
    private List<String> warmUp = new ArrayList<>();

    private List<ExerciseDetailDto> exercises = new ArrayList<>();

    private List<String> coolDown = new ArrayList<>();

}
