package se.knowit.bookitnotification.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        incomingNotification.getParticipant().setEmail("");
        assertThrows(IllegalArgumentException.class, () -> validator.validateNotificationOrElseThrowException(incomingNotification));
    }
}