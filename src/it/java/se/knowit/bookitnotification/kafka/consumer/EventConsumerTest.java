package se.knowit.bookitnotification.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import se.knowit.bookitnotification.model.Event;
import se.knowit.bookitnotification.repository.EventRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(partitions = 1,
        topics = {"registrations", "event"},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class EventConsumerTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private KafkaTemplate<String, Event> kafkaTemplate;

    @BeforeEach
    void setup() {
        kafkaTemplate = kafkaTemplate();
    }

    @Test
    void test_consumeEvent() throws InterruptedException {
        Event event = createEvent();
        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry.getListenerContainer("event-listener");
        container.stop();
        AcknowledgingConsumerAwareMessageListener<String, Event> messageListener = (AcknowledgingConsumerAwareMessageListener<String, Event>) container.getContainerProperties().getMessageListener();
        CountDownLatch latch = new CountDownLatch(1);
        container.getContainerProperties().setMessageListener((AcknowledgingConsumerAwareMessageListener<String, Event>) (record, acknowledgment, consumer) -> {
            messageListener.onMessage(record, acknowledgment, consumer);
            latch.countDown();
        });
        container.start();
        kafkaTemplate.send("event", event);
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
        Event savedEvent = eventRepository.findByEventId(event.getEventId()).orElseThrow();
        Assertions.assertEquals(savedEvent, event);
    }

    private Event createEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setEventId(UUID.randomUUID());
        event.setName("test event");
        event.setDescription("description");
        event.setOrganizer("org");
        event.setLocation("loc");
        event.setDeadlineRVSP(Instant.ofEpochMilli(10000L));
        event.setEventStart(Instant.ofEpochMilli(20000L));
        event.setEventEnd(Instant.ofEpochMilli(30000L));
        return event;
    }

    private ProducerFactory<String, Event> producerFactory() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>(mapper));
    }

    private KafkaTemplate<String, Event> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
