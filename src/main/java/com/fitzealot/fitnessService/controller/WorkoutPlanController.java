package com.fitzealot.fitnessService.controller;


import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.model.entity.WorkoutPlan;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/workout-plans")
public class WorkoutPlanController {

    // Define endpoints for managing workout plans here
    // For example, you might have methods to create, update, delete, and retrieve workout plans

    private final WorkoutPlanService workoutPlanService;



    @PostMapping("/generate-plan")
    public Mono<ResponseEntity<WorkoutPlanDto>> generatePlan(@RequestBody WorkoutRequest request) {
        Mono<ResponseEntity<WorkoutPlanDto>> responseEntityMono = workoutPlanService.generateAndSaveWorkoutPlan(request)
                .map(ResponseEntity::ok)
                .doOnError(e -> System.err.println("Error in controller: " + e.getMessage()));


        System.out.println(responseEntityMono);

        return responseEntityMono;

    }
    @PostMapping("/update")
    public void updateWorkoutPlan() {
        // Logic to update a workout plan
    }

    @PostMapping("/delete")
    public void deleteWorkoutPlan() {
        // Logic to delete a workout plan
    }

    @PostMapping("/get")
    public void getWorkoutPlan() {
        // Logic to retrieve a specific workout plan
    }

    @PostMapping("/get-all")
    public void getAllWorkoutPlans() {
        // Logic to retrieve all workout plans
    }

}
