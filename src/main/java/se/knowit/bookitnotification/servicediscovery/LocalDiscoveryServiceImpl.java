package se.knowit.bookitnotification.servicediscovery;

import org.springframework.core.env.Environment;

public class LocalDiscoveryServiceImpl implements DiscoveryService {

    private final Environment environment;
    private final static String prefix = "discovery.service.";

    public LocalDiscoveryServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Instance discoverInstance(String serviceName) {
        Instance instance = null;
        String address = environment.getProperty(prefix + serviceName);

        if(address != null) {
            instance = new Instance();
            String[] ipPort = address.split(":");
            instance.setInstanceIpv4(ipPort[0]);
            instance.setInstancePort(ipPort[1]);
        }
        return instance;
    }

}
