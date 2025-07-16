package com.fitzealot.fitnessService.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class WorkoutPlan {

    @Id
    private String id;
    private String userId;
    private String title;
    private int durationWeeks;
    private String goal; // Muscle Gain, Fat Loss
    private LocalDate startDate;
    private LocalDate endDate;


}
