package se.knowit.bookitnotification.servicediscovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AwsDiscoveryServiceImplTests {

    @InjectMocks
    private AwsDiscoveryServiceImpl discoveryService;

    @Mock
    AWSServiceDiscovery discoveryServiceClient;

    @Test
    public void testDiscoverInstance() {
        String serviceName = "fakeService";
        String ipv4 = "0.0.0.0";
        String port = "80";
        String region = "test-region";

        Instance expected = new Instance();
        expected.setInstanceIpv4(ipv4);
        expected.setInstancePort(port);
        expected.setRegion(region);
        DiscoverInstancesRequest request = createDiscoverInstanceRequest(serviceName);
        DiscoverInstancesResult result = createDiscoverInstanceResult(ipv4, port, region);

        when(discoveryServiceClient.discoverInstances(eq(request))).thenReturn(result);
        Instance instance = discoveryService.discoverInstance(serviceName);
        Assertions.assertEquals(expected, instance);
    }

    @Test
    public void testDiscoverInstanceNonExisting() {

    }

    private DiscoverInstancesRequest createDiscoverInstanceRequest(String serviceName) {
        return new DiscoverInstancesRequest().withServiceName(serviceName);
    }

    private DiscoverInstancesResult createDiscoverInstanceResult(String ipv4, String port, String region) {
        HttpInstanceSummary instanceSummary = new HttpInstanceSummary();
        Map<String,String> instanceAttr = new HashMap<>();
        instanceAttr.put("AWS_INSTANCE_IPV4", ipv4);
        instanceAttr.put("AWS_INSTANCE_PORT", port);
        instanceAttr.put("REGION", region);
        instanceSummary.setAttributes(instanceAttr);
        instanceSummary.setServiceName("fakeService");
        return new DiscoverInstancesResult().withInstances(instanceSummary);
    }
}
