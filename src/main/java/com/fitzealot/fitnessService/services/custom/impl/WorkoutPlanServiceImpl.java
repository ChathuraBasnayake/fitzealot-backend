package com.fitzealot.fitnessService.services.custom.impl;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final ModelMapper modelMapper;
    private final GeminiService geminiService ;

    @Override
    public Mono<WorkoutPlanDto> generateAndSaveWorkoutPlan(WorkoutRequest request) {
        return geminiService.generateAndSaveWorkoutPlan(request)
                .map(plan -> modelMapper.map(plan, WorkoutPlanDto.class));
    }




}


