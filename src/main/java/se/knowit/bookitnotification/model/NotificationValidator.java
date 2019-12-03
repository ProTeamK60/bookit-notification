package se.knowit.bookitnotification.model;

import java.util.UUID;

public class NotificationValidator {

    public Notification validateNotificationOrElseThrowException(Notification notification) {
        validateEventId(notification.getEventId());
        validateParticipant(notification.getParticipant());
        return notification;
    }

    private void validateEventId(UUID eventId) {
        if(eventId == null || eventId.toString().isBlank()) {
            throw new IllegalArgumentException("Invalid eventId: " + eventId);
        }
    }

    private void validateParticipant(Participant participant) {
        if(participant == null) {
            throw new IllegalArgumentException("Null participant");
        }
        if(isNullOrBlank(participant.getEmail()) || !participant.getEmail().matches("^(.+)@(.+)$")) {
            throw new IllegalArgumentException("Invalid email " + participant.getEmail());
        }
    }

    private boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

}
