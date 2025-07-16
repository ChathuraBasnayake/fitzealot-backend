package com.fitzealot.fitnessService.controller;

import com.fitzealot.fitnessService.model.dto.WorkoutRequest;
import com.fitzealot.fitnessService.model.entity.WorkoutPlan;
import com.fitzealot.fitnessService.services.custom.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/fitnessDetailsController")
public class FitnessDetailsController {



    // This controller will handle requests related to fitness details.
    // You can define endpoints here to manage fitness details, such as creating,
    // updating, retrieving, and deleting fitness records.



    @PostMapping("/create")
    void createFitnessDetails() {
        // Logic to create fitness details
    }
    @PostMapping("/update")
    void updateFitnessDetails() {
        // Logic to update fitness details
    }

    @PostMapping("/delete")
    void deleteFitnessDetails() {
        // Logic to delete fitness details
    }

    @PostMapping("/get")
    void getFitnessDetails() {
        // Logic to retrieve fitness details
    }

    @PostMapping("/getAll")
    void getAllFitnessDetails() {
        // Logic to retrieve fitness details
    }
}
