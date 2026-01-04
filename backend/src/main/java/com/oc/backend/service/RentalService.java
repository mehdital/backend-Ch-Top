package com.oc.backend.service;

import com.oc.backend.domain.rental.Rental;
import com.oc.backend.domain.user.User;
import com.oc.backend.dto.rental.RentalCreateRequest;
import com.oc.backend.dto.rental.RentalDetailResponse;
import com.oc.backend.dto.rental.RentalSummaryResponse;
import com.oc.backend.dto.rental.RentalUpdateRequest;
import com.oc.backend.dto.rental.RentalsResponse;
import com.oc.backend.dto.rental.RentalResponse;
import com.oc.backend.repository.RentalRepository;
import com.oc.backend.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
/**
 * Service métier des rentals.
 *
 * <p>Responsabilités :
 * <ul>
 *   <li>Lister et mapper les entités {@link Rental} vers des DTO de réponse</li>
 *   <li>Créer / mettre à jour un rental, y compris la sauvegarde d'une photo sur disque</li>
 *   <li>Construire l'URL publique de la photo via {@link com.oc.backend.config.WebConfig}</li>
 * </ul>
 */
public class RentalService {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;
  private final Path uploadDir;

  public RentalService(
      RentalRepository rentalRepository,
      UserRepository userRepository,
      @Value("${app.upload-dir}") String uploadDir) {
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
    this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  public RentalsResponse list() {
    List<RentalSummaryResponse> rentals = rentalRepository.findAll().stream()
        .map(this::toSummary)
        .toList();
    return new RentalsResponse(rentals);
  }

  public RentalDetailResponse findById(Integer id) {
    Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new RentalDetailResponse(
        rental.getId(),
        rental.getName(),
        rental.getSurface(),
        rental.getPrice(),
        List.of(rental.getPicture()),
        rental.getDescription(),
        rental.getOwner().getId(),
        formatDate(rental.getCreatedAt()),
        formatDate(rental.getUpdatedAt()));
  }

  public RentalResponse create(RentalCreateRequest request, String email, String baseUrl) {
    User owner = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

    if (request.getPicture() == null || request.getPicture().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Rental rental = new Rental();
    rental.setName(request.getName());
    rental.setSurface(request.getSurface());
    rental.setPrice(request.getPrice());
    rental.setDescription(request.getDescription());
    rental.setOwner(owner);
    // Sauvegarde l'image sur disque puis stocke l'URL publique dans la colonne RENTALS.picture.
    rental.setPicture(storePicture(request.getPicture(), baseUrl));
    rentalRepository.save(rental);
    return new RentalResponse("Rental created !");
  }

  public RentalResponse update(Integer id, RentalUpdateRequest request, String baseUrl) {
    Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    rental.setName(request.getName());
    rental.setSurface(request.getSurface());
    rental.setPrice(request.getPrice());
    rental.setDescription(request.getDescription());

    if (request.getPicture() != null && !request.getPicture().isEmpty()) {
      // Si une nouvelle image est fournie, on la sauvegarde et on remplace l'URL existante.
      rental.setPicture(storePicture(request.getPicture(), baseUrl));
    }

    rentalRepository.save(rental);
    return new RentalResponse("Rental updated !");
  }

  private RentalSummaryResponse toSummary(Rental rental) {
    return new RentalSummaryResponse(
        rental.getId(),
        rental.getName(),
        rental.getSurface(),
        rental.getPrice(),
        rental.getPicture(),
        rental.getDescription(),
        rental.getOwner().getId(),
        formatDate(rental.getCreatedAt()),
        formatDate(rental.getUpdatedAt()));
  }

  private String formatDate(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return value.toLocalDate().format(DATE_FORMAT);
  }

  // Sauvegarde l'image uploadee sur disque et renvoie l'URL publique a stocker en base.
  private String storePicture(MultipartFile file, String baseUrl) {
    try {
      // 1) Cree le dossier d'upload si besoin.
      Files.createDirectories(uploadDir);

      // 2) Nettoie le nom original et ne garde que l'extension (si presente).
      String originalName = StringUtils.cleanPath(file.getOriginalFilename());
      String extension = StringUtils.getFilenameExtension(originalName);

      // 3) Genere un nom unique pour eviter les collisions et ne pas exposer le nom fourni par l'utilisateur.
      String filename = UUID.randomUUID() + (extension == null ? "" : "." + extension);
      Path target = uploadDir.resolve(filename);

      // 4) Copie le flux sur disque et renvoie l'URL publique servie via /uploads/**.
      Files.copy(file.getInputStream(), target);
      return baseUrl + "/uploads/" + filename;
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
