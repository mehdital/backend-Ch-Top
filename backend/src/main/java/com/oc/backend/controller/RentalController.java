package com.oc.backend.controller;

import com.oc.backend.dto.rental.RentalDetailResponse;
import com.oc.backend.dto.rental.RentalCreateRequest;
import com.oc.backend.dto.rental.RentalResponse;
import com.oc.backend.dto.rental.RentalsResponse;
import com.oc.backend.dto.rental.RentalUpdateRequest;
import com.oc.backend.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rentals")
@SecurityRequirement(name = "bearerAuth")
/**
 * Contrôleur des locations (rentals).
 *
 * <p>Gère :
 * <ul>
 *   <li>liste des rentals</li>
 *   <li>détail d'un rental</li>
 *   <li>création / mise à jour avec upload de photo (multipart/form-data)</li>
 * </ul>
 */
public class RentalController {
  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @Operation(summary = "Get all rentals")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rentals list"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public RentalsResponse list() {
    return rentalService.list();
  }

  @Operation(summary = "Get rental by id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rental detail"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RentalDetailResponse detail(@PathVariable Integer id) {
    return rentalService.findById(id);
  }

  @Operation(summary = "Create a rental")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rental created"),
      @ApiResponse(responseCode = "400", description = "Invalid payload"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public RentalResponse create(@Valid @ModelAttribute RentalCreateRequest request, Authentication auth) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return rentalService.create(request, auth.getName(), baseUrl);
  }

  @Operation(summary = "Update a rental")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rental updated"),
      @ApiResponse(responseCode = "400", description = "Invalid payload"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public RentalResponse update(
      @PathVariable Integer id,
      @Valid @ModelAttribute RentalUpdateRequest request) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return rentalService.update(id, request, baseUrl);
  }
}
