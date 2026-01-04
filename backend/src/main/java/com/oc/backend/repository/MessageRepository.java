package com.oc.backend.repository;

import com.oc.backend.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès base de données pour les {@link Message}.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {}
