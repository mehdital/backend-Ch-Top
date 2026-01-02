package com.oc.backend.dto.rental;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RentalSummaryResponse(
    Integer id,
    String name,
    BigDecimal surface,
    BigDecimal price,
    String picture,
    String description,
    @JsonProperty("owner_id") Integer ownerId,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt
) {}
