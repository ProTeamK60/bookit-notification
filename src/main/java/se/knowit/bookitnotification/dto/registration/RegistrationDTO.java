package se.knowit.bookitnotification.dto.registration;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String eventId;
    private String registrationId;
    private ParticipantDTO participant;
}
