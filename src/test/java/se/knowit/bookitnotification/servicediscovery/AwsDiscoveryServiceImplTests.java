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

import java.util.Collections;
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
        String namespaceName = "fakeNamespace";
        String serviceName = "fakeService";
        String ipv4 = "0.0.0.0";
        String port = "80";
        String region = "test-region";

        Instance expected = new Instance();
        expected.setInstanceIpv4(ipv4);
        expected.setInstancePort(port);
        expected.setRegion(region);
        DiscoverInstancesRequest request = new DiscoverInstancesRequest().withNamespaceName(namespaceName).withServiceName(serviceName);
        DiscoverInstancesResult result = createDiscoverInstancesResult(serviceName, ipv4, port, region);

        when(discoveryServiceClient.discoverInstances(eq(request))).thenReturn(result);
        DiscoveryServiceResult discoveryResult = discoveryService.discoverInstances(namespaceName, serviceName);

        Assertions.assertEquals(1, discoveryResult.getInstances().size());
        Assertions.assertEquals(expected, discoveryResult.getInstances().get(0));
    }

    @Test
    public void testDiscoverInstanceNonExisting() {
        String namespaceName = "fakeNamespace";
        String serviceName = "fakeService";
        DiscoverInstancesRequest request = new DiscoverInstancesRequest().withNamespaceName(namespaceName).withServiceName(serviceName);
        DiscoverInstancesResult result = new DiscoverInstancesResult().withInstances(Collections.emptyList());

        when(discoveryServiceClient.discoverInstances(eq(request))).thenReturn(result);
        DiscoveryServiceResult discoveryResult = discoveryService.discoverInstances(namespaceName, serviceName);
        Assertions.assertTrue(discoveryResult.getInstances().isEmpty());
    }


    private DiscoverInstancesResult createDiscoverInstancesResult(String serviceName, String ipv4, String port, String region) {
        HttpInstanceSummary instanceSummary = new HttpInstanceSummary();
        Map<String,String> instanceAttr = new HashMap<>();
        instanceAttr.put("AWS_INSTANCE_IPV4", ipv4);
        instanceAttr.put("AWS_INSTANCE_PORT", port);
        instanceAttr.put("REGION", region);
        instanceSummary.setAttributes(instanceAttr);
        instanceSummary.setServiceName(serviceName);
        return new DiscoverInstancesResult().withInstances(instanceSummary);
    }

}
