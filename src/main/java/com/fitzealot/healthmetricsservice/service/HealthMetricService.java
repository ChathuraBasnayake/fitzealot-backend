package com.fitzealot.healthmetricsservice.service;

public interface HealthMetricService {

    double calculateBMI(double weightKg, double heightCm);
    int calculateCalories(Long userId);
    double calculateProtein(Long userId);


}
