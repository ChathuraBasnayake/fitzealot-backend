package com.fitzealot.fitnessService.services.custom;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.gemini.WorkoutRequest;

import java.util.List;

public interface WorkoutPlanService {

    WorkoutPlanDto generateAndSaveWorkoutPlan(WorkoutRequest request);


    WorkoutPlanDto get(String id);

    WorkoutPlanDto update(String id, WorkoutPlanDto workoutPlanDto);

    List<WorkoutPlanDto> getAll();

    void delete(String id);
}
