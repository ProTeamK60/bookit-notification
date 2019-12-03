package se.knowit.bookitnotification.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private String eventId;
    private ParticipantDTO participant;
}