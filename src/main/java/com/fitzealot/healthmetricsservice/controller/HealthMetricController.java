package com.fitzealot.healthmetricsservice.controller;

import com.fitzealot.healthmetricsservice.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health-metrics")
public class HealthMetricController {

    public final HealthMetricService healthMetricService;

    @GetMapping("/calculate")
    public double calculateBMI(@RequestParam double weightKg, @RequestParam double heightMeters) {
        return healthMetricService.calculateBMI(weightKg, heightMeters);
    }


}
