package com.oc.backend.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
/**
 * Filtre d'authentification JWT exécuté une fois par requête.
 *
 * <p>Étapes (JWT) :
 * <ol>
 *   <li>Lire l'en-tête HTTP {@code Authorization}.</li>
 *   <li>Vérifier le préfixe {@code Bearer } puis extraire le token.</li>
 *   <li>Valider le token (signature + expiration) via {@link JwtService}.</li>
 *   <li>Extraire l'identité (ici l'email dans le {@code subject}).</li>
 *   <li>Créer une {@code Authentication} et la mettre dans le {@code SecurityContext}.</li>
 *   <li>Laisser la requête continuer dans la chaîne de filtres.</li>
 * </ol>
 *
 * <p>Remarque : cette implémentation ne charge pas les rôles (authorities) et utilise une liste vide.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);
    try {
      if (jwtService.isTokenValid(token)
          && SecurityContextHolder.getContext().getAuthentication() == null) {
        String username = jwtService.extractUsername(token);
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, null, List.of());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    } catch (JwtException ex) {
      filterChain.doFilter(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
