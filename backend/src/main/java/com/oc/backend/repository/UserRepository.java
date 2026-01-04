package com.oc.backend.repository;

import com.oc.backend.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès base de données pour {@link User}.
 *
 * <p>Ajoute une recherche par email utilisée pour le login et pour l'endpoint {@code /auth/me}.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}
