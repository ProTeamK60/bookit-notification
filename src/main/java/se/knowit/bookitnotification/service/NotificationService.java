package se.knowit.bookitnotification.service;

import se.knowit.bookitnotification.dto.NotificationMailDTO;
import se.knowit.bookitnotification.model.Notification;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface NotificationService {
    void sendRegistrationNotificationAsync(Notification notification);
    void sendRegistrationNotification(Notification notification);
    void sendMailAsync(NotificationMailDTO mail);
    void sendMail(NotificationMailDTO mail) throws MessagingException;
}
