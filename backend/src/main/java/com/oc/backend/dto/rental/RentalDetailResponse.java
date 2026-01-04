package com.oc.backend.dto.rental;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de réponse pour le détail d'une location.
 *
 * <p>Inclut une liste de photos (ici une seule URL), et des dates formatées pour l'API.
 */
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
