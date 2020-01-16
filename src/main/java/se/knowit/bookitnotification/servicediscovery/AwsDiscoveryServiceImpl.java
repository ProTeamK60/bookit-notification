package se.knowit.bookitnotification.servicediscovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;

public class AwsDiscoveryServiceImpl implements DiscoveryService {

    private final AWSServiceDiscovery serviceDiscoverClient;

    public AwsDiscoveryServiceImpl(AWSServiceDiscovery serviceDiscoveryClient) {
        this.serviceDiscoverClient = serviceDiscoveryClient;
    }

    @Override
    public Instance discoverInstance(String serviceName) {
        Instance instance = null;
        DiscoverInstancesResult discoverInstancesResult = serviceDiscoverClient.discoverInstances(
                new DiscoverInstancesRequest().withServiceName(serviceName));

        if (!discoverInstancesResult.getInstances().isEmpty()) {
            HttpInstanceSummary instanceSummary = discoverInstancesResult.getInstances().get(0);
            instance = new Instance();
            instance.setInstanceIpv4(instanceSummary.getAttributes().get("AWS_INSTANCE_IPV4"));
            instance.setInstancePort(instanceSummary.getAttributes().get("AWS_INSTANCE_PORT"));
            instance.setRegion(instanceSummary.getAttributes().get("REGION"));
        }

        return instance;
    }
}
