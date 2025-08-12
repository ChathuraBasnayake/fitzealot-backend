package com.fitzealot.healthmetricsservice.controller;

import com.fitzealot.healthmetricsservice.model.dto.CalorieCalculationRequestDTO;
import com.fitzealot.healthmetricsservice.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health-metrics")
public class HealthMetricController {

    private final HealthMetricService healthMetricService;

    @GetMapping("/calculate-bmi")
    public ResponseEntity<Double> calculateBMI(@RequestParam double weightKg, @RequestParam double heightMeters) {
        Double bmi = healthMetricService.calculateBMI(weightKg, heightMeters);
        return ResponseEntity.ok(bmi);
    }

    @GetMapping("/calculate-protein")
    public ResponseEntity<Double> calculateProtein(@RequestParam double weightKg) {
        Double bmi = healthMetricService.calculateProtein(weightKg);
        return ResponseEntity.ok(bmi);
    }

    @PostMapping("/calculate-calories")
    public ResponseEntity<Double> calculateCalories(@RequestBody CalorieCalculationRequestDTO dto) {
        Double calories = healthMetricService.calculateBMR(dto);
        return ResponseEntity.ok(calories);
    }
}
