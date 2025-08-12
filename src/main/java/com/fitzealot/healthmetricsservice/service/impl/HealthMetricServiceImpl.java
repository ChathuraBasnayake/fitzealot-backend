package com.fitzealot.healthmetricsservice.service.impl;

import com.fitzealot.healthmetricsservice.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HealthMetricServiceImpl implements HealthMetricService {

    private final HealthMetricService healthMetricService;


    @Override
    public double calculateBMI(double weightKg, double heightMeters) {
        if (heightMeters <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero");
        }
        return weightKg / (heightMeters * heightMeters);
    }


    @Override
    public int calculateCalories(Long userId) {
        return 0;
    }

    @Override
    public double calculateProtein(Long userId) {
        return 0;
    }
}
