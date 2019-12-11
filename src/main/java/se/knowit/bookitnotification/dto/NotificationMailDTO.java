package se.knowit.bookitnotification.dto;

import lombok.Data;

@Data
public class NotificationMailDTO {
    private RecipientDTO[] recipients;
    private String Subject;
    private BodyPartDTO[] bodyParts;
}
