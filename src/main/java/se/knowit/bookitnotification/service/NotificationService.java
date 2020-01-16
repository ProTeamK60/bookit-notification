package se.knowit.bookitnotification.service;

import se.knowit.bookitnotification.model.Notification;

public interface NotificationService {
    void sendRegistrationNotification(Notification notification);
}
