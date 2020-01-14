package se.knowit.bookitnotification.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID eventId;
    private UUID registrationId;
    @OneToOne
    @JoinColumn(name="id")
    private Participant participant;
}
