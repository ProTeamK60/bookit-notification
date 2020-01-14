package se.knowit.bookitnotification.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.Registration;
import se.knowit.bookitnotification.service.NotificationService;

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
    private void processRegistration(Registration registration) {
        //TODO: log with DEBUG level instead.
        System.out.println("RECEIVED REGISTRATION: " + registration);
        notificationService.sendRegistrationNotification(buildNotification(registration));
    }

    private Notification buildNotification(Registration registration) {
        Notification notification = new Notification();
        notification.setEventId(registration.getEventId());
        notification.setParticipant(registration.getParticipant());
        return notification;
    }

}
