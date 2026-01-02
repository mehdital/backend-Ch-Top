package com.oc.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    Integer id,
    String name,
    String email,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt
) {}
