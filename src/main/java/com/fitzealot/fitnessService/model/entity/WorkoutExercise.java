package com.fitzealot.fitnessService.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WorkoutExercise {

    @Id
    private String userId;
    private String day; // e.g., "Monday", or "Day 1"
    private String exerciseName;
    private int sets;
    private int reps;
}
