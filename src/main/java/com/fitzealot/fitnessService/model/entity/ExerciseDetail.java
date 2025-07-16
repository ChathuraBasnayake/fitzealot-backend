package com.fitzealot.fitnessService.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ExerciseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int sets;
    private String reps;
}
