package com.fitzealot.fitnessService.services.custom;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface WorkoutPlanService {

    Mono<WorkoutPlanDto> generateAndSaveWorkoutPlan(WorkoutRequest request);


    Mono<WorkoutPlanDto> get(String id);

    Mono<ResponseEntity<Object>> update(String id, WorkoutPlanDto workoutPlanDto);
}
