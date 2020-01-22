package se.knowit.bookitnotification.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import se.knowit.bookitnotification.dto.event.EventDTO;
import se.knowit.bookitnotification.dto.registration.RegistrationDTO;
import se.knowit.bookitnotification.kafka.consumer.EventConsumer;
import se.knowit.bookitnotification.kafka.consumer.RegistrationConsumer;
import se.knowit.bookitnotification.repository.EventRepository;
import se.knowit.bookitnotification.service.NotificationService;
import se.knowit.bookitnotification.servicediscovery.DiscoveryService;
import se.knowit.bookitnotification.servicediscovery.DiscoveryServiceResult;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    private final DiscoveryService discoveryService;
    
    public KafkaConsumerConfiguration(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }
    
    @Bean
    public Map<String, Object> consumerConfig() {
        DiscoveryServiceResult result = discoveryService.discoverInstances("bookit", "kafka");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                result.getAddresses());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, EventDTO> eventConsumerFactory() {
        Map<String, Object> props = consumerConfig();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "event-consumer-group");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(EventDTO.class, false));
    }

    @Bean
    public ConsumerFactory<String, RegistrationDTO> registrationConsumerFactory() {
        Map<String, Object> props = consumerConfig();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "registration-consumer-group");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(RegistrationDTO.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventConsumerFactory());
        return factory;
    }

    @Bean(name = "registrationListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, RegistrationDTO> registrationListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RegistrationDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(registrationConsumerFactory());
        return factory;
    }

    @Bean
    @Autowired
    public EventConsumer eventConsumer(EventRepository repository) {
        return new EventConsumer(repository);
    }

    @Bean
    @Autowired
    public RegistrationConsumer registrationConsumer(NotificationService notificationService) {
        return new RegistrationConsumer(notificationService);
    }

}
