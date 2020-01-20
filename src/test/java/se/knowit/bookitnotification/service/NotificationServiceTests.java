package se.knowit.bookitnotification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.Participant;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTests {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();

    @InjectMocks
    private NotificationServiceImpl service;

    @Mock
    private JavaMailSender mailSender;

    private SimpleMailMessage message;

    @BeforeEach
    void setup() {
        message = new SimpleMailMessage();
    }

    @Test
    void testSendNotification() {
        Notification notification = new Notification();
        notification.setEventId(DEFAULT_UUID);
        Participant participant = new Participant();
        participant.setEmail("test@test.com");
        notification.setParticipant(participant);
        message.setTo(participant.getEmail());
        message.setSubject("Confirmation: registration for event " + notification.getEventId());
        message.setText("You are now registered for this event!");
        doNothing().when(mailSender).send(eq(message));
        service.sendRegistrationNotification(notification);
    }
}
