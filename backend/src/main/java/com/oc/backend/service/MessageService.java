package com.oc.backend.service;

import com.oc.backend.domain.message.Message;
import com.oc.backend.domain.rental.Rental;
import com.oc.backend.domain.user.User;
import com.oc.backend.dto.message.MessageRequest;
import com.oc.backend.dto.message.MessageResponse;
import com.oc.backend.repository.MessageRepository;
import com.oc.backend.repository.RentalRepository;
import com.oc.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;

  public MessageService(
      MessageRepository messageRepository,
      UserRepository userRepository,
      RentalRepository rentalRepository) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.rentalRepository = rentalRepository;
  }

  public MessageResponse create(MessageRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    Rental rental = rentalRepository.findById(request.rentalId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

    Message message = new Message();
    message.setUser(user);
    message.setRental(rental);
    message.setMessage(request.message());
    messageRepository.save(message);

    return new MessageResponse("Message send with success");
  }
}
