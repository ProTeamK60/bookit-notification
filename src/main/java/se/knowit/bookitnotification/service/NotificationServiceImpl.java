package se.knowit.bookitnotification.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import se.knowit.bookitnotification.model.NotificationValidator;
import se.knowit.bookitnotification.model.Notification;

public class NotificationServiceImpl implements NotificationService {

    private JavaMailSender mailSender;

    private NotificationValidator validator;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.validator = new NotificationValidator();
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendRegistrationNotificationAsync(Notification notification) {
        try {
            sendRegistrationNotification(notification);
        } catch(IllegalArgumentException | MailException e) {
            //TODO: log exception.
            e.printStackTrace();
        }
    }

    @Override
    public void sendRegistrationNotification(Notification notification) {
        Notification validNotification = validator.validateNotificationOrElseThrowException(notification);
        mailSender.send(fromRegistrationTemplate(validNotification));
    }

    private SimpleMailMessage fromRegistrationTemplate(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getParticipant().getEmail());
        //TODO: Put more useful information in the email and make it html-format.
        message.setSubject("Confirmation: registration for event " + notification.getEventId());
        message.setText("You are now registered for this event!");
        return message;
    }

}
