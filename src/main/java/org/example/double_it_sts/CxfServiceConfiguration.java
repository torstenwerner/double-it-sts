package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.apache.cxf.rt.security.SecurityConstants.*;

@Configuration
public class CxfServiceConfiguration {
    @Bean
    public Endpoint endpoint(Bus bus, DoubleItPortType service) {
        EndpointImpl endpoint = new EndpointImpl(bus, service);

        final Map<String, Object> inProps = Map.of();
        endpoint.getInInterceptors().add(new WSS4JInInterceptor(inProps));
        final Map<String, Object> outProps = Map.of();
        endpoint.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));

        endpoint.getProperties().put(CALLBACK_HANDLER, new ClientCallbackHandler());
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "client");
        endpoint.getProperties().put(ENCRYPT_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(ENCRYPT_USERNAME, "client");

        endpoint.publish("/double-it");
        return endpoint;
    }
}
