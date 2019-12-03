package se.knowit.bookitnotification.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Notification {
    private UUID eventId;
    private Participant participant;
}
