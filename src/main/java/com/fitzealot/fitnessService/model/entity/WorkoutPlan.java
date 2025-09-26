package com.fitzealot.fitnessService.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class WorkoutPlan {

    @Id
    private String username;

    // **NEW FIELD ADDED** to match the database table
    private int durationWeeks;

    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_plan_id")
    private List<DailyWorkout> weeklyPlan = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
