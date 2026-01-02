package com.oc.backend.repository;

import com.oc.backend.domain.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Integer> {}
