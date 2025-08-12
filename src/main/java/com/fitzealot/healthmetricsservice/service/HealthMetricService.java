package com.fitzealot.healthmetricsservice.service;

import com.fitzealot.healthmetricsservice.model.dto.CalorieCalculationRequestDTO;

public interface HealthMetricService {

    Double calculateBMI(double weightKg, double heightMeters);

    Double calculateBMR(CalorieCalculationRequestDTO dto);

    Double calculateProtein(Double weightKg);
}
