package com.oc.backend.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
    @JsonProperty("rental_id") @NotNull Integer rentalId,
    @JsonProperty("user_id") @NotNull Integer userId,
    @NotBlank String message
) {}
