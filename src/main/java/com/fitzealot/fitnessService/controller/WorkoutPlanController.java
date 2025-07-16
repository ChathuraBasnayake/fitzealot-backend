package com.fitzealot.fitnessService.controller;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/workout-plans")
public class WorkoutPlanController {

    // Define endpoints for managing workout plans here
    // For example, you might have methods to create, update, delete, and retrieve workout plans

    private final WorkoutPlanService workoutPlanService;


    @PostMapping("/generate-plan")
    public Mono<ResponseEntity<WorkoutPlanDto>> generatePlan(@RequestBody WorkoutRequest request) {

        return workoutPlanService.generateAndSaveWorkoutPlan(request)
                .map(ResponseEntity::ok)
                .doOnError(e -> System.err.println("Error in controller: " + e.getMessage()));

    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Object>> updateWorkoutPlan(
            @PathVariable String id,
            @RequestBody WorkoutPlanDto dto) {
        System.out.println(dto);
        return workoutPlanService.update(id, dto);
    }


    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteWorkoutPlan() {

        return null;

        // Logic to delete a workout plan
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<WorkoutPlanDto>> getWorkoutPlan(@PathVariable String id) {
        return workoutPlanService.get(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }


    @GetMapping("/get-all")
    ResponseEntity<List<WorkoutPlanDto>> getAllWorkoutPlans() {

        return null;
        // Logic to retrieve all workout plans
    }

}
