package com.fitzealot.dietservice.controller;

import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.dto.response.DietPlanResponse;
import com.fitzealot.dietservice.service.custom.DietPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/diet")
@Validated
@RequiredArgsConstructor
public class DietControler {

    private final DietPlanService dietPlanService;

    @PostMapping("/createDietPlan")
    public ResponseEntity<DietPlanResponse> createUser(@Valid @RequestBody DietPlanRequest dietPlanRequest) {
        try {
            DietPlanResponse dietPlan = dietPlanService.createDietPlan(dietPlanRequest);
            return ResponseEntity.ok(dietPlan);
        } catch (Exception e) {
            log.error("Error generating workout plan", e);
            return ResponseEntity.internalServerError().build();
        }
    }




}
