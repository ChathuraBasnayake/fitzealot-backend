package com.fitzealot.dietservice.service.event; // <--- Make sure this package is correct

import com.fitzealot.dietservice.model.dto.request.DietPlanRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Corrected: was Slf44j
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;
import java.util.UUID;

@Slf4j // Corrected
@Component // <--- THIS IS CRUCIAL! Ensures Spring detects it as a bean
@RequiredArgsConstructor
public class DietPlanEventsPublisher {

    // If using Spring's internal events for simplicity as in the publisher
    // In a real microservices setup with Kafka/RabbitMQ,
    // you would inject the specific broker's template here (e.g., KafkaTemplate).
    private final ApplicationEventPublisher eventPublisher;

    // Define topic names (e.g., in application.properties or constants)
    private static final String DIET_PLAN_REQUEST_TOPIC = "diet.plan.requested";
    private static final String DIET_PLAN_GENERATED_TOPIC = "diet.plan.generated";
    private static final String DIET_PLAN_GENERATION_FAILED_TOPIC = "diet.plan.generation.failed";


    public void publishDietPlanRequestedEvent(DietPlanRequest request) {
        log.info("Publishing DietPlanRequestedEvent for user: {}", request.userId());
        eventPublisher.publishEvent(request); // For demonstration with Spring's internal events
        // For Kafka: kafkaTemplate.send(DIET_PLAN_REQUEST_TOPIC, request.userId(), request);
    }

    public void publishDietPlanGeneratedEvent(String userId, UUID dietPlanId, String status) {
        log.info("Publishing DietPlanGeneratedEvent for user: {}, planId: {}", userId, dietPlanId);
        eventPublisher.publishEvent(new DietPlanGeneratedEvent(userId, dietPlanId, status, "Diet plan generated successfully!"));
        // For Kafka: kafkaTemplate.send(DIET_PLAN_GENERATED_TOPIC, userId, Map.of("userId", userId, "dietPlanId", dietPlanId, "status", status));
    }

    public void publishDietPlanGenerationFailedEvent(String userId, String reason) {
        log.warn("Publishing DietPlanGenerationFailedEvent for user: {}, reason: {}", userId, reason);
        eventPublisher.publishEvent(new DietPlanGenerationFailedEvent(userId, UUID.randomUUID().toString(), reason, null));
        // For Kafka: kafkaTemplate.send(DIET_PLAN_GENERATION_FAILED_TOPIC, userId, Map.of("userId", userId, "reason", reason));
    }

    // --- Example Event DTOs for internal Spring events (or for direct broker publishing) ---
    // In a real scenario, these would likely be in a shared 'common-events' module
    public record DietPlanGeneratedEvent(String userId, UUID dietPlanId, String status, String message) {}
    public record DietPlanGenerationFailedEvent(String userId, String requestId, String reason, String timestamp) {}
}