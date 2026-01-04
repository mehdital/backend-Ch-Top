package com.oc.backend.config;

import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
/**
 * Gestionnaire global d'exceptions pour retourner une réponse JSON cohérente.
 *
 * <p>Pour ce projet, on choisit de renvoyer un body JSON vide ({@code {}} / map vide)
 * afin de coller au format attendu par le front / les tests d'intégration.
 */
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    // Retourne un JSON vide en cas d'erreur de validation
    return ResponseEntity.badRequest().body(Collections.emptyMap());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<Map<String, Object>> handleBind(BindException ex) {
    return ResponseEntity.badRequest().body(Collections.emptyMap());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, Object>> handleStatus(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(Collections.emptyMap());
  }
}
