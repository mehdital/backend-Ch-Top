package com.oc.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload JSON reçu pour la connexion.
 */
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {}
