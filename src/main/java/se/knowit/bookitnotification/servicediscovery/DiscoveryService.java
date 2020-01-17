package se.knowit.bookitnotification.servicediscovery;

public interface DiscoveryService {
    DiscoveryServiceResult discoverInstances(String serviceName);
}
