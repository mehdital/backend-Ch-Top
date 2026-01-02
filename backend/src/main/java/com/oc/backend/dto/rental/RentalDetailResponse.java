package com.oc.backend.dto.rental;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record RentalDetailResponse(
    Integer id,
    String name,
    BigDecimal surface,
    BigDecimal price,
    List<String> picture,
    String description,
    @JsonProperty("owner_id") Integer ownerId,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt
) {}
