package com.fitzealot.dietservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MacrosResponse(
        String protein,
        String carbs,
        String fat
) {}