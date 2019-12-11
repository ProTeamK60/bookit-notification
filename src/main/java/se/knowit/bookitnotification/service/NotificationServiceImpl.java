package se.knowit.bookitnotification.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import se.knowit.bookitnotification.dto.BodyPartDTO;
import se.knowit.bookitnotification.dto.NotificationMailDTO;
import se.knowit.bookitnotification.dto.RecipientDTO;
import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.NotificationValidator;

import javax.mail.MessagingException;
import javax.mail.internet.*;

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
    @Async
    public void sendMailAsync(NotificationMailDTO notification) {
        try {
            sendMail(notification);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(NotificationMailDTO notification) throws MessagingException {
        MimeMessage message = prepareMail(notification);
        mailSender.send(message);
    }

    private MimeMessage prepareMail(NotificationMailDTO notification) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        for(RecipientDTO recipient : notification.getRecipients()) {
            message.addRecipient(
                    MimeMessage.RecipientType.BCC,
                    new InternetAddress(recipient.getEmail())
            );
        }
        message.setSubject(notification.getSubject());
        MimeMultipart multipart = new MimeMultipart();
        for(BodyPartDTO bodyPartDto : notification.getBodyParts()) {
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(bodyPartDto.getContent(), bodyPartDto.getContentType());
            multipart.addBodyPart(bodyPart);
        }
        message.setContent(multipart);
        return message;
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
