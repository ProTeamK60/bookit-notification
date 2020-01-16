package se.knowit.bookitnotification.servicediscovery;

import lombok.Data;

@Data
public class Instance {

    private String instanceIpv4;
    private String instancePort;
    private String region;

    public String getAddress() {
        return this.instanceIpv4 + ":" + instancePort;
    }

}
