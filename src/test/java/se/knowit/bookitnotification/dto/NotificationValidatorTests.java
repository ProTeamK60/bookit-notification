package se.knowit.bookitnotification.dto;

import org.junit.jupiter.api.Test;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.NotificationValidator;
import se.knowit.bookitnotification.model.Participant;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationValidatorTests {

    private static final UUID DEFAULT_EVENT_ID = UUID.randomUUID();
    private final NotificationValidator validator = new NotificationValidator();

    @Test
    public void testValidateAValidNotificationShouldReturnValidNotification() {
        Notification incomingNotification = new Notification();
        incomingNotification.setEventId(DEFAULT_EVENT_ID);
        incomingNotification.setParticipant(new Participant());
        incomingNotification.getParticipant().setEmail("email@valid.com");
        Notification validNotification = validator.validateNotificationOrElseThrowException(incomingNotification);
        assertEquals(incomingNotification.getEventId(), validNotification.getEventId());
        assertEquals(incomingNotification.getParticipant(), validNotification.getParticipant());
    }

    @Test
    public void testNotificationWithInvalidEventIdShouldThrowException() {
        Notification incomingNotification = new Notification();
        incomingNotification.setParticipant(new Participant());
        incomingNotification.getParticipant().setEmail("email@valid.com");
        assertThrows(IllegalArgumentException.class, () -> validator.validateNotificationOrElseThrowException(incomingNotification));
    }

    @Test
    public void testNotificationWithInvalidParticipantShouldThrowException() {
        Notification incomingNotification = new Notification();
        incomingNotification.setEventId(DEFAULT_EVENT_ID);
        assertThrows(IllegalArgumentException.class, () -> validator.validateNotificationOrElseThrowException(incomingNotification));
    }

    @Test
    public void testNotificationWIthInvalidEmailShouldThrowException() {
        Notification incomingNotification = new Notification();
        incomingNotification.setEventId(DEFAULT_EVENT_ID);
        incomingNotification.setParticipant(new Participant());
        assertThrows(IllegalArgumentException.class, () -> validator.validateNotificationOrElseThrowException(incomingNotification));
    }
}