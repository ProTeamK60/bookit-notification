package se.knowit.bookitnotification.dto;

import se.knowit.bookitnotification.model.Notification;
import se.knowit.bookitnotification.model.Participant;

import java.util.UUID;

public class NotificationMapper {

    public Notification fromDto(NotificationDTO dto) {
        Notification notification = new Notification();
        if(dto.getEventId() != null) {
            notification.setEventId(UUID.fromString(dto.getEventId()));
        }
        if(dto.getParticipant() != null) {
            Participant participant = new Participant();
            participant.setEmail(dto.getParticipant().getEmail());
            notification.setParticipant(participant);
        }
        return notification;
    }
}
