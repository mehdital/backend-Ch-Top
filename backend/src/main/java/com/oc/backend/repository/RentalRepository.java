package com.oc.backend.repository;

import com.oc.backend.domain.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès base de données pour les {@link Rental}.
 */
public interface RentalRepository extends JpaRepository<Rental, Integer> {}
