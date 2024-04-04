package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.cxf.rt.security.SecurityConstants.*;

@Configuration
public class CxfServiceConfiguration {
    @Bean
    public Endpoint endpoint(Bus bus, DoubleItPortType service) {
        EndpointImpl endpoint = new EndpointImpl(bus, service);

        endpoint.getProperties().put(CALLBACK_HANDLER, new ClientCallbackHandler());
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "client");

        endpoint.publish("/double-it");
        return endpoint;
    }
}
