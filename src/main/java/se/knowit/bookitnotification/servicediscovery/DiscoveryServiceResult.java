package se.knowit.bookitnotification.servicediscovery;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ServiceDiscoveryResult {

    private List<Instance> instances;

    public ServiceDiscoveryResult() {
        this.instances = new ArrayList();
    }

    public void addInstance(Instance instance) {
        this.instances.add(instance);
    }

    public String getAddresses() {
        return instances.stream().map(instance -> instance.getAddress()).collect(Collectors.joining(","));
    }
}
