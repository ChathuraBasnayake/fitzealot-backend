package com.fitzealot.fitnessService.model.dto.gemini;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiExerciseDto(String name, int sets, String reps) {
}

