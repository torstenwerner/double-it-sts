package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfServiceConfiguration {
    @Bean
    public Endpoint endpoint(Bus bus, DoubleItPortType doubleItPortType) {
        EndpointImpl endpoint = new EndpointImpl(bus, doubleItPortType);
        endpoint.publish("/double-it");
        return endpoint;
    }
}
