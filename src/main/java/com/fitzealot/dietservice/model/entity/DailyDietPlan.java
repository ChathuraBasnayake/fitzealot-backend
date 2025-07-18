package com.fitzealot.dietservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "daily_diet_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyDietPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id", nullable = false)
    private DietPlan dietPlan;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private boolean isRestDay;

    @OneToMany(mappedBy = "dailyDietPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> mealPlan = new ArrayList<>();

    @Column(length = 1000)
    private String notes;

    public void addMeal(Meal meal) {
        mealPlan.add(meal);
        meal.setDailyDietPlan(this);
    }
}