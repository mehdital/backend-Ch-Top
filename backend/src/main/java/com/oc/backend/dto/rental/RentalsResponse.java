package com.oc.backend.dto.rental;

import java.util.List;

public record RentalsResponse(List<RentalSummaryResponse> rentals) {}
