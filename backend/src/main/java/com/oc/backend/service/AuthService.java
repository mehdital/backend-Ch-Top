package com.oc.backend.service;

import com.oc.backend.domain.user.User;
import com.oc.backend.dto.auth.AuthResponse;
import com.oc.backend.dto.auth.LoginRequest;
import com.oc.backend.dto.auth.RegisterRequest;
import com.oc.backend.dto.user.UserResponse;
import com.oc.backend.repository.UserRepository;
import com.oc.backend.security.JwtService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email());
    // Hash before
    user.setPassword(passwordEncoder.encode(request.password()));

    User saved = userRepository.save(user);
    return new AuthResponse(jwtService.generateToken(saved));
  }

  public Optional<AuthResponse> login(LoginRequest request) {
    return userRepository.findByEmail(request.email())
        .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
        .map(user -> new AuthResponse(jwtService.generateToken(user)));
  }

  public UserResponse me(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
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
