package com.fitzealot.healthmetricsservice.service.impl;

import com.fitzealot.healthmetricsservice.model.dto.CalorieCalculationRequestDTO;
import com.fitzealot.healthmetricsservice.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HealthMetricServiceImpl implements HealthMetricService {

    @Override
    public Double calculateBMI(double weightKg, double heightMeters) {
        if (heightMeters <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero to calculate BMI.");
        }
        return weightKg / (heightMeters * heightMeters);
    }

    @Override
    public Double calculateBMR(CalorieCalculationRequestDTO dto) {
        if (dto.isMale()) {
            return (10 * dto.getWeightKg()) + (6.25 * dto.getHeightCm()) - (5 * dto.getAgeYears()) + 5;
        } else {
            return (10 * dto.getWeightKg()) + (6.25 * dto.getHeightCm()) - (5 * dto.getAgeYears()) - 161;
        }
    }

    @Override
    public Double calculateProtein(Double weightKg) {
        final double standardProteinPerKg = 0.8;
        return weightKg * standardProteinPerKg;
    }
}
