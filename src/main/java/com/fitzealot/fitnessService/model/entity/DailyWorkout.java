package com.fitzealot.fitnessService.model.entity;

import com.fitzealot.fitnessService.model.entity.ExerciseDetail;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class DailyWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek;
    private boolean isRestDay;
    private String workoutType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "warmup_steps", joinColumns = @JoinColumn(name = "daily_workout_id"))
    @Column(name = "step")
    private List<String> warmUp = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "daily_workout_id")
    private List<ExerciseDetail> exercises = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cooldown_steps", joinColumns = @JoinColumn(name = "daily_workout_id"))
    @Column(name = "step")
    private List<String> coolDown = new ArrayList<>();
}
