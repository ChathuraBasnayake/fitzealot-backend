package com.fitzealot.dietservice.repository;

import com.fitzealot.dietservice.model.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, UUID> {

    DietPlan findByUserId(String userId);

}
