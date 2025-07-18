package com.fitzealot.dietservice.model.entity;

import com.fitzealot.dietservice.util.DietPlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "diet_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DietPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate generationDate;

    @Column(nullable = false)
    private String fitnessGoal;

    @Column(nullable = false)
    private String activityLevel;

    @Column(nullable = false)
    private Integer mealsPerDay;

    @ElementCollection
    @CollectionTable(name = "diet_plan_dietary_preferences", joinColumns = @JoinColumn(name = "diet_plan_id"))
    @Column(name = "preference")
    private List<String> dietaryPreferences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "diet_plan_allergies", joinColumns = @JoinColumn(name = "diet_plan_id"))
    @Column(name = "allergy")
    private List<String> allergies = new ArrayList<>();

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "dietPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyDietPlan> weeklyPlan = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DietPlanStatus status;

    @PrePersist
    protected void onCreate() {
        if (generationDate == null) {
            generationDate = LocalDate.now();
        }
        if (status == null) {
            status = DietPlanStatus.GENERATED;
        }
    }

    public void addDailyDietPlan(DailyDietPlan dailyPlan) {
        weeklyPlan.add(dailyPlan);
        dailyPlan.setDietPlan(this);
    }
}
