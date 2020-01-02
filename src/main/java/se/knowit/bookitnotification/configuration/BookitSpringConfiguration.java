package se.knowit.bookitnotification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.knowit.bookitnotification.service.NotificationService;
import se.knowit.bookitnotification.service.NotificationServiceImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    public NotificationService notificationServiceImpl() { return new NotificationServiceImpl(); }

}
