package com.fitzealot.fitnessService.repository;

import com.fitzealot.fitnessService.model.entity.FitnessDetails;
import com.fitzealot.fitnessService.model.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan,String> {
}
