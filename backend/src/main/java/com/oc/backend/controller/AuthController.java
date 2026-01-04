package com.oc.backend.controller;

import com.oc.backend.dto.auth.AuthResponse;
import com.oc.backend.dto.auth.LoginRequest;
import com.oc.backend.dto.auth.RegisterRequest;
import com.oc.backend.dto.user.UserResponse;
import com.oc.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
/**
 * Contrôleur d'authentification.
 *
 * <p>Endpoints :
 * <ul>
 *   <li>{@code POST /auth/register} : création de compte + retour d'un JWT</li>
 *   <li>{@code POST /auth/login} : vérification des identifiants + retour d'un JWT</li>
 *   <li>{@code GET /auth/me} : informations du user courant (JWT requis)</li>
 * </ul>
 */
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

  @Operation(summary = "Login a user")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login success"),
      @ApiResponse(responseCode = "401", description = "Invalid credentials")
  })
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Collections.singletonMap("message", "error")));
  }

  @Operation(summary = "Get current user")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Current user"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserResponse me(Authentication authentication) {
    return authService.me(authentication.getName());
  }
}
