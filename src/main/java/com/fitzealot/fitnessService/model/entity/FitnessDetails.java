package com.fitzealot.fitnessService.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FitnessDetails {

    @Id
    private String id;
    private String userId;
    private double heightCm;
    private double weightKg;
    private String fitnessGoal;
    private String activityLevel;
    private String preferredWorkoutTime;
    private int workoutDaysPerWeek;


}
