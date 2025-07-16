package com.fitzealot.fitnessService.repository;

import com.fitzealot.fitnessService.model.entity.FitnessDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FitnessDetailsRepository extends JpaRepository<FitnessDetails,String> {
}
