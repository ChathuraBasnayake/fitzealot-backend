package com.fitzealot.fitnessService.services.custom;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import reactor.core.publisher.Mono;

public interface WorkoutPlanService {

    public Mono<WorkoutPlanDto> generateAndSaveWorkoutPlan(WorkoutRequest request);


}
