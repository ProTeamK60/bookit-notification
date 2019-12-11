package se.knowit.bookitnotification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.knowit.bookitnotification.dto.NotificationDTO;
import se.knowit.bookitnotification.dto.NotificationMailDTO;
import se.knowit.bookitnotification.dto.NotificationMapper;
import se.knowit.bookitnotification.service.NotificationService;

@RestController
@RequestMapping(NotificationController.BASE_PATH)
public class NotificationController {
    static final String BASE_PATH = "/api/v1/notifications";
    private final NotificationService service;
    private final NotificationMapper mapper;

    public NotificationController(NotificationService service) {
        this.service = service;
        this.mapper = new NotificationMapper();
    }

    @PostMapping(value={"/registration", "/registration/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createRegistrationConfirmation(@RequestBody NotificationDTO dto) {
        service.sendRegistrationNotificationAsync(mapper.fromDto(dto));
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = {"/email", "/email/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendEmail(@RequestBody NotificationMailDTO dto) {
        service.sendMailAsync(dto);
        return ResponseEntity.noContent().build();
    }

}
