package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.cxf.rt.security.SecurityConstants.*;

/**
 * Creates the endpoint for the DoubleIt service.
 */
@Configuration
public class CxfServiceConfiguration {
    @Bean
    public Endpoint endpoint(Bus bus, DoubleItPortType service) {
        EndpointImpl endpoint = new EndpointImpl(bus, service);

        endpoint.getProperties().put(CALLBACK_HANDLER, new CallbackHandler("service"));
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "serverstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "server");

        endpoint.publish("/double-it");
        return endpoint;
    }
}
