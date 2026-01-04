package com.oc.backend.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Déclare le schéma de sécurité "bearerAuth" pour Swagger/OpenAPI.
 *
 * <p>Permet d'utiliser un JWT dans l'en-tête {@code Authorization: Bearer <token>}
 * directement depuis Swagger UI.
 */
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {}
