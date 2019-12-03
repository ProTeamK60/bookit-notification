package se.knowit.bookitnotification.service;

import se.knowit.bookitnotification.model.Notification;

public interface NotificationService {
    void sendRegistrationNotificationAsync(Notification notification);
    void sendRegistrationNotification(Notification notification);
}
