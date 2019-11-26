package se.knowit.bookitnotification.dto;

import org.junit.jupiter.api.Test;
import se.knowit.bookitnotification.model.Notification;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationMapperTests {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private NotificationMapper mapper = new NotificationMapper();

    @Test
    public void testMappingDto() {
        NotificationDTO dto = new NotificationDTO();
        dto.setEventId(DEFAULT_UUID.toString());
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setEmail("test@test.com");
        dto.setParticipant(participantDTO);
        Notification mappedNotification = mapper.fromDto(dto);
        assertEquals(DEFAULT_UUID, mappedNotification.getEventId());
        assertEquals(dto.getParticipant().getEmail(), mappedNotification.getParticipant().getEmail());
    }
}