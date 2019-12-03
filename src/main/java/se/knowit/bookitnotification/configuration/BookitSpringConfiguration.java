package se.knowit.bookitnotification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import se.knowit.bookitnotification.service.NotificationService;
import se.knowit.bookitnotification.service.NotificationServiceImpl;

import java.util.Properties;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    @Autowired
    public NotificationService notificationServiceImpl(JavaMailSender mailSender) { return new NotificationServiceImpl(mailSender); }

}
