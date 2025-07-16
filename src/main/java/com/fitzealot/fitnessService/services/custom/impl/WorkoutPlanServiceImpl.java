package com.fitzealot.fitnessService.services.custom.impl;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.model.entity.DailyWorkout;
import com.fitzealot.fitnessService.repository.WorkoutPlanRepository;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final ModelMapper modelMapper;
    private final GeminiService geminiService;
    private final WorkoutPlanRepository workoutPlanRepository;

    @Override
    public Mono<WorkoutPlanDto> generateAndSaveWorkoutPlan(WorkoutRequest request) {
        return geminiService.generateAndSaveWorkoutPlan(request)
                .map(plan -> modelMapper.map(plan, WorkoutPlanDto.class));
    }

    @Override
    public Mono<WorkoutPlanDto> get(String id) {
        return Mono.fromCallable(() ->
                modelMapper.map(
                        workoutPlanRepository.findById(id).orElseThrow(
                                () -> new RuntimeException("Workout plan not found with id: " + id)
                        ),
                        WorkoutPlanDto.class
                )
        );
    }


    @Override
    public Mono<ResponseEntity<Object>> update(String id, WorkoutPlanDto workoutPlanDto) {
        return Mono.fromCallable(() -> {
            var existingPlan = workoutPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Workout plan not found with id: " + id));

            existingPlan.setDurationWeeks(workoutPlanDto.getDurationWeeks());

            List<DailyWorkout> updatedList = workoutPlanDto.getWeeklyPlan().stream()
                    .map(dto -> modelMapper.map(dto, DailyWorkout.class))
                    .collect(Collectors.toList());

            existingPlan.getWeeklyPlan().clear(); // Clear the existing list
            existingPlan.getWeeklyPlan().addAll(updatedList); // Add new mapped items

            workoutPlanRepository.save(existingPlan);

            return ResponseEntity.ok().build();
        });
    }


}


