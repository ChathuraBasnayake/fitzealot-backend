package com.fitzealot.dietservice.controller;

import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import com.fitzealot.dietservice.model.dto.response.DietPlanResponse;
import com.fitzealot.dietservice.service.custom.DietPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/diet")
@Validated
@RequiredArgsConstructor
public class DietControler {

    private final DietPlanService dietPlanService;

    @PostMapping("/generate-plan")
    public ResponseEntity<DietPlanResponse> createUser(@Valid @RequestBody DietPlanRequest dietPlanRequest) {
        try {
            DietPlanResponse dietPlan = dietPlanService.createDietPlan(dietPlanRequest);
            return ResponseEntity.ok(dietPlan);
        } catch (Exception e) {
            log.error("Error generating diet plan", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get/{username}")
    ResponseEntity<DietPlanResponse> getDietPlan(@PathVariable String username) {
        try {
            DietPlanResponse dietPlan = dietPlanService.getDietPlanByUsername(username);
            return ResponseEntity.ok(dietPlan);
        } catch (Exception e) {
            log.error("Error getting diet plan", e);
            return ResponseEntity.internalServerError().build();
        }


    }


    @PutMapping("/update")
    ResponseEntity<DietPlanResponse> updateDietPlan(@Valid @RequestBody DietPlanRequest dietPlanRequest) {
        try {
            return ResponseEntity.ok(dietPlanService.update(dietPlanRequest));
        } catch (Exception e) {
            log.error("Error updating diet plan", e);
            return ResponseEntity.internalServerError().build();
        }

    }


}
