package com.oc.backend.service;

import com.oc.backend.domain.user.User;
import com.oc.backend.dto.user.UserResponse;
import com.oc.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserResponse findById(Integer id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        formatDate(user.getCreatedAt()),
        formatDate(user.getUpdatedAt()));
  }

  private String formatDate(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return value.toLocalDate().format(DATE_FORMAT);
  }
}
