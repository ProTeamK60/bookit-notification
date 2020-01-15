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
import se.knowit.bookitnotification.model.Participant;
import se.knowit.bookitnotification.model.Registration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@EmbeddedKafka(partitions = 1,
        topics = {RegistrationConsumerTest.TOPIC, "event"},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class RegistrationConsumerTest {

    public static final String TOPIC = "registrations";

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private KafkaTemplate<String, Registration> kafkaTemplate;

    @BeforeEach
    public void setup() {
        kafkaTemplate = createKafkaTemplate();
    }

    @Test
    public void test_consumeRegistration() throws InterruptedException {
        ConcurrentMessageListenerContainer<?, ?> container =
                (ConcurrentMessageListenerContainer<?, ?>) registry.getListenerContainer("registration-listener");
        container.stop();
        AcknowledgingConsumerAwareMessageListener<String, Registration> messageListener =
                (AcknowledgingConsumerAwareMessageListener<String, Registration>) container.getContainerProperties().getMessageListener();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Registration> receivedRegistration = new AtomicReference<>();

        container.getContainerProperties()
                .setMessageListener((AcknowledgingConsumerAwareMessageListener<String, Registration>) (record, acknowledgment, consumer) -> {
                    receivedRegistration.set(record.value());
                    latch.countDown();
                });
        container.start();

        Registration registration = createDefaultRegistration();
        kafkaTemplate.send(TOPIC, registration);
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
        Assertions.assertEquals(registration, receivedRegistration.get());
    }

    private ProducerFactory<String, Registration> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new DefaultKafkaProducerFactory(props, new StringSerializer(), new JsonSerializer(mapper));
    }

    private KafkaTemplate<String, Registration> createKafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }

    private Registration createDefaultRegistration() {
        Registration registration = new Registration();
        registration.setId(1L);
        registration.setEventId(UUID.randomUUID());
        registration.setRegistrationId(UUID.randomUUID());
        registration.setParticipant(new Participant());
        registration.getParticipant().setEmail("test@test.com");
        registration.getParticipant().setId(1L);
        return registration;
    }

}
