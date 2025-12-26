package com.oc.backend.controller;

import com.oc.backend.dto.auth.AuthResponse;
import com.oc.backend.dto.auth.RegisterRequest;
import com.oc.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Register a new user")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User registered"),
      @ApiResponse(responseCode = "400", description = "Invalid payload")
  })
  @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }
}
