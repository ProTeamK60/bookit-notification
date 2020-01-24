package se.knowit.bookitnotification.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import se.knowit.bookitnotification.dto.event.EventDTO;
import se.knowit.bookitnotification.dto.event.EventMapper;
import se.knowit.bookitnotification.repository.EventRepository;

public class EventConsumer {

    private static final String TOPIC = "events";
    private final EventRepository repository;
    private final EventMapper mapper;

    public EventConsumer(EventRepository repository) {
        this.repository = repository;
        this.mapper = new EventMapper();
    }

    @KafkaListener(id = "event-listener",
            topics = EventConsumer.TOPIC,
            groupId = "notification-consumer-group",
            topicPartitions = @TopicPartition(
                    topic = EventConsumer.TOPIC,
                    partitionOffsets = @PartitionOffset(
                            partition = "0",
                            initialOffset = "0")))
    private void consumeMessage(EventDTO event) {
        //TODO: log event received with DEBUG level.
        repository.save(mapper.fromDTO(event));
    }


}
