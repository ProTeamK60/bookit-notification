package se.knowit.bookitnotification.servicediscovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HealthStatusFilter;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;
import org.springframework.beans.factory.annotation.Value;

public class AwsDiscoveryServiceImpl implements DiscoveryService {

    private final AWSServiceDiscovery service;

    @Value("${discover.aws.statusfilter}")
    private HealthStatusFilter statusFilter;

    public AwsDiscoveryServiceImpl() {
        service = AWSServiceDiscoveryClientBuilder.defaultClient();
    }

    @Override
    public String discoverInstance(String serviceName) {
        DiscoverInstancesResult discoverInstancesResult = service.discoverInstances(
                new DiscoverInstancesRequest()
                        .withServiceName(serviceName)
                        .withHealthStatus(statusFilter));
        String address = null;
        if(!discoverInstancesResult.getInstances().isEmpty()) {
            HttpInstanceSummary instanceSummary = discoverInstancesResult.getInstances().get(0);
            address = instanceSummary.getAttributes().get("AWS_INSTANCE_IPV4");
        }
        return address;
    }
}
