package se.knowit.bookitnotification.kafka.consumer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.knowit.bookitnotification.dto.registration.ParticipantDTO;
import se.knowit.bookitnotification.dto.registration.RegistrationDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@EmbeddedKafka(partitions = 1,
        topics = {RegistrationConsumerTest.EVENT_TOPIC, RegistrationConsumerTest.REGISTRATION_TOPIC},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RegistrationConsumerTest {

    public static final String REGISTRATION_TOPIC = "registrations";
    public static final String EVENT_TOPIC = "events";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private KafkaTemplate<String, RegistrationDTO> kafkaTemplate;

    @BeforeEach
    public void setup() {
        kafkaTemplate = createKafkaTemplate();
    }

    @Test
    public void test_consumeRegistration() throws InterruptedException {
        ConcurrentMessageListenerContainer<?, ?> container =
                (ConcurrentMessageListenerContainer<?, ?>) registry.getListenerContainer("registration-listener");
        container.stop();
        AcknowledgingConsumerAwareMessageListener<String, RegistrationDTO> messageListener =
                (AcknowledgingConsumerAwareMessageListener<String, RegistrationDTO>) container.getContainerProperties().getMessageListener();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<RegistrationDTO> receivedRegistration = new AtomicReference<>();

        container.getContainerProperties()
                .setMessageListener((AcknowledgingConsumerAwareMessageListener<String, RegistrationDTO>) (record, acknowledgment, consumer) -> {
                    receivedRegistration.set(record.value());
                    latch.countDown();
                });
        container.start();

        RegistrationDTO registration = createDefaultRegistration();
        kafkaTemplate.send(REGISTRATION_TOPIC, registration);
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
        Assertions.assertEquals(registration, receivedRegistration.get());
    }

    private ProducerFactory<String, RegistrationDTO> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>());
    }

    private KafkaTemplate<String, RegistrationDTO> createKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private RegistrationDTO createDefaultRegistration() {
        RegistrationDTO registration = new RegistrationDTO();
        registration.setEventId(UUID.randomUUID().toString());
        registration.setRegistrationId(UUID.randomUUID().toString());
        registration.setParticipant(new ParticipantDTO());
        registration.getParticipant().setEmail("test@test.com");
        return registration;
    }

}
