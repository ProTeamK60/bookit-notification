package se.knowit.bookitnotification.servicediscovery;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DiscoveryServiceResult {

    private List<Instance> instances;

    public DiscoveryServiceResult() {
        this.instances = new ArrayList<>();
    }

    public void addInstance(Instance instance) {
        this.instances.add(instance);
    }

    public String getAddresses() {
        return instances.stream().map(Instance::getAddress).collect(Collectors.joining(","));
    }
}
