package se.knowit.bookitnotification.servicediscovery;

import org.springframework.core.env.Environment;

public class LocalDiscoveryServiceImpl implements DiscoveryService {

    private final Environment environment;
    private final static String prefix = "discovery.service.";

    public LocalDiscoveryServiceImpl(Environment environment) { this.environment = environment; }

    @Override
    public String discoverInstance(String serviceName) {
        return environment.getProperty(prefix + serviceName);
    }

}
