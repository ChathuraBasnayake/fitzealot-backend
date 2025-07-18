package com.fitzealot.dietservice.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiMacrosDto(
        @JsonProperty("protein") String protein,
        @JsonProperty("carbs") String carbs,
        @JsonProperty("fat") String fat
) {}