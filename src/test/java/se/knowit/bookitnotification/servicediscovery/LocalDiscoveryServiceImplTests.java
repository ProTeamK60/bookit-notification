package se.knowit.bookitnotification.servicediscovery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocalDiscoveryServiceImplTests {

    @InjectMocks
    private LocalDiscoveryServiceImpl service;

    @Mock
    private Environment env;

    @Test
    public void testDiscoverInstance() {
        String address = "0.0.0.0:80";
        when(env.getProperty(eq("discovery.service.testService"))).thenReturn(address);
        Instance instance = service.discoverInstance("testService");
        Assertions.assertEquals(address, instance.getAddress());
    }

    @Test
    public void testDiscoverInstanceNonExisting() {
        when(env.getProperty(any())).thenReturn(null);
        Instance instance = service.discoverInstance("testService");
        Assertions.assertNull(instance);
    }

}
