package com.fitzealot.healthmetricsservice.model.dto;


import lombok.Data;

@Data
public class CalorieCalculationRequestDTO {
    private boolean isMale;
    private double weightKg;
    private double heightCm;
    private int ageYears;
    private double activityFactor;
}