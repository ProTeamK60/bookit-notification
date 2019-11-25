package se.knowit.bookitnotification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.Participant;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTests {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();

    @InjectMocks
    private NotificationServiceImpl service;

    @Mock
    private JavaMailSender mailSender;

    private SimpleMailMessage message;

    @BeforeEach
    public void setup() {
        message = new SimpleMailMessage();
    }

    @Test
    public void testSendNotification() {
        Notification notification = new Notification();
        notification.setEventId(DEFAULT_UUID);
        Participant participant = new Participant();
        participant.setEmail("test@test.com");
        notification.setParticipant(participant);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        service.sendRegistrationNotification(notification);
    }
}
