package com.fitzealot.fitnessService.controller;

import com.fitzealot.fitnessService.exception.ResourceNotFoundException;
import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.gemini.WorkoutRequest;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/workout-plans")
@Validated
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping("/generate-plan")
    public ResponseEntity<WorkoutPlanDto> generatePlan(@Valid @RequestBody WorkoutRequest request) {
        try {
            WorkoutPlanDto dto = workoutPlanService.generateAndSaveWorkoutPlan(request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error generating workout plan", e);
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WorkoutPlanDto> updateWorkoutPlan(
            @PathVariable String id,
            @Valid @RequestBody WorkoutPlanDto dto) {
        try {
            WorkoutPlanDto updatedDto = workoutPlanService.update(id, dto);
            return ResponseEntity.ok(updatedDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating workout plan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable String id) {
        try {
            workoutPlanService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting workout plan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WorkoutPlanDto> getWorkoutPlan(@PathVariable String id) {
        try {
            WorkoutPlanDto dto = workoutPlanService.get(id);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error retrieving workout plan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<WorkoutPlanDto>> getAllWorkoutPlans() {
        try {
            List<WorkoutPlanDto> list = workoutPlanService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error retrieving all workout plans", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}