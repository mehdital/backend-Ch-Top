package com.oc.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de réponse exposant les informations publiques d'un utilisateur.
 */
public record UserResponse(
    Integer id,
    String name,
    String email,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt
) {}
