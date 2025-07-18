package com.fitzealot.fitnessService.services.custom.impl;

import com.fitzealot.fitnessService.exception.ResourceNotFoundException;
import com.fitzealot.fitnessService.exception.ServiceException;
import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.gemini.WorkoutRequest;
import com.fitzealot.fitnessService.model.entity.DailyWorkout;
import com.fitzealot.fitnessService.repository.WorkoutPlanRepository;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final ModelMapper modelMapper;
    private final GeminiService geminiService;
    private final WorkoutPlanRepository workoutPlanRepository;

    @Override
    public WorkoutPlanDto generateAndSaveWorkoutPlan(WorkoutRequest request) {
        try {
            var plan = geminiService.generateAndSaveWorkoutPlan(request);
            return modelMapper.map(plan, WorkoutPlanDto.class);
        } catch (Exception e) {
            log.error("Error generating workout plan", e);
            throw new ServiceException("Failed to generate workout plan", e);
        }
    }

    @Override
    public WorkoutPlanDto get(String id) {
        var plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + id));
        try {
            return modelMapper.map(plan, WorkoutPlanDto.class);
        } catch (Exception e) {
            log.error("Error mapping workout plan", e);
            throw new ServiceException("Failed to map workout plan", e);
        }
    }

    @Override
    public WorkoutPlanDto update(String id, WorkoutPlanDto workoutPlanDto) {
        if (workoutPlanDto == null) {
            throw new IllegalArgumentException("Workout plan DTO cannot be null");
        }

        var existingPlan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + id));

        try {
            existingPlan.setDurationWeeks(workoutPlanDto.getDurationWeeks());

            List<DailyWorkout> updatedList = workoutPlanDto.getWeeklyPlan().stream()
                    .map(dto -> modelMapper.map(dto, DailyWorkout.class))
                    .toList();

            existingPlan.getWeeklyPlan().clear();
            existingPlan.getWeeklyPlan().addAll(updatedList);

            var savedPlan = workoutPlanRepository.save(existingPlan);
            return modelMapper.map(savedPlan, WorkoutPlanDto.class);
        } catch (Exception e) {
            log.error("Error updating workout plan with id: {}", id, e);
            throw new ServiceException("Failed to update workout plan", e);
        }
    }

    @Override
    public List<WorkoutPlanDto> getAll() {
        try {
            var plans = workoutPlanRepository.findAll();
            return plans.stream()
                    .map(plan -> modelMapper.map(plan, WorkoutPlanDto.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving all workout plans", e);
            throw new ServiceException("Failed to retrieve workout plans", e);
        }
    }

    @Override
    public void delete(String id) {
        if (!workoutPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout plan not found with id: " + id);
        }
        workoutPlanRepository.deleteById(id);
    }
}