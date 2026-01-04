package com.oc.backend.dto.rental;

import java.util.List;

/**
 * Wrapper de réponse pour la liste des locations.
 */
public record RentalsResponse(List<RentalSummaryResponse> rentals) {}
