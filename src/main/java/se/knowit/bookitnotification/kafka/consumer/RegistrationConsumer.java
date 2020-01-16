package se.knowit.bookitnotification.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import se.knowit.bookitnotification.dto.registration.ParticipantDTO;
import se.knowit.bookitnotification.dto.registration.RegistrationDTO;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.Participant;
import se.knowit.bookitnotification.service.NotificationService;

import java.util.UUID;

public class RegistrationConsumer {

    private static final String TOPIC = "registrations";
    private final NotificationService notificationService;

    public RegistrationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(id = "registration-listener",
            topics = RegistrationConsumer.TOPIC,
            groupId = "notification-consumer-group",
            containerFactory = "registrationListenerContainerFactory")
    private void processRegistration(RegistrationDTO registration) {
        //TODO: log registration received with DEBUG level instead.
        notificationService.sendRegistrationNotification(buildNotification(registration));
    }

    private Notification buildNotification(RegistrationDTO registration) {
        Notification notification = new Notification();
        notification.setEventId(UUID.fromString(registration.getEventId()));
        notification.setParticipant(fromParticipantDTO(registration.getParticipant()));
        return notification;
    }

    private Participant fromParticipantDTO(ParticipantDTO dto) {
        Participant participant = new Participant();
        participant.setEmail(dto.getEmail());
        return participant;
    }

}
