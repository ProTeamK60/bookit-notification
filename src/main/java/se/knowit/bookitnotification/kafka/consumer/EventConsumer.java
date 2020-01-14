package se.knowit.bookitnotification.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import se.knowit.bookitnotification.model.Event;
import se.knowit.bookitnotification.repository.EventRepository;

public class EventConsumer {

    private static final String TOPIC = "event";
    private final EventRepository repository;

    public EventConsumer(EventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(id = "event-listener",
            topics = EventConsumer.TOPIC,
            groupId = "notification-consumer-group",
            topicPartitions = @TopicPartition(
                    topic = EventConsumer.TOPIC,
                    partitionOffsets = @PartitionOffset(
                            partition = "0",
                            initialOffset = "0")))
    public void processEvent(Event event) {
        //TODO: log event with DEBUG level instead.
        System.out.println("RECEIVED EVENT: " + event);
        repository.save(event);
    }


}
