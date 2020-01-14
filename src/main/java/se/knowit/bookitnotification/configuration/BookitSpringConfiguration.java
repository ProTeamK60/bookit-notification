package se.knowit.bookitnotification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.knowit.bookitnotification.repository.EventRepository;
import se.knowit.bookitnotification.repository.map.EventRepositoryMapImpl;
import se.knowit.bookitnotification.service.NotificationService;
import se.knowit.bookitnotification.service.NotificationServiceImpl;
import se.knowit.bookitnotification.servicediscovery.AwsDiscoveryServiceImpl;
import se.knowit.bookitnotification.servicediscovery.DiscoveryService;
import se.knowit.bookitnotification.servicediscovery.LocalDiscoveryServiceImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    public NotificationService notificationServiceImpl() { return new NotificationServiceImpl(); }

    @Bean
    public EventRepository mapBasedEventRepositoryImpl() { return new EventRepositoryMapImpl(); }

    @Profile("dev")
    @Bean
    @Autowired
    public DiscoveryService LocalDiscoveryServiceImpl(Environment environment) {
        return new LocalDiscoveryServiceImpl(environment);
    }

    @Profile("prod")
    @Bean
    public DiscoveryService AwsDiscoveryServiceImpl() { return new AwsDiscoveryServiceImpl(); }

}
