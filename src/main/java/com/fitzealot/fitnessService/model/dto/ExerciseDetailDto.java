package com.fitzealot.fitnessService.model.dto;

import lombok.Data;

@Data
public class ExerciseDetailDto {

    private Long id;
    private String name;
    private int sets;
    private String reps;


}
