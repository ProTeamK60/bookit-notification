package se.knowit.bookitnotification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.knowit.bookitnotification.servicediscovery.DiscoveryService;
import se.knowit.bookitnotification.servicediscovery.LocalDiscoveryServiceImpl;

@Profile("dev")
@Configuration
public class DevConfiguration {

    @Bean
    @Autowired
    public DiscoveryService localDiscoveryServiceImpl(Environment environment) {
        return new LocalDiscoveryServiceImpl(environment);
    }

}
