package com.oc.backend.service;

import com.oc.backend.domain.user.User;
import com.oc.backend.dto.auth.AuthResponse;
import com.oc.backend.dto.auth.RegisterRequest;
import com.oc.backend.repository.UserRepository;
import com.oc.backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
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
}
