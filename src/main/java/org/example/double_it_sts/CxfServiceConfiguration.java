package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.wss4j.common.ConfigurationConstants;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.cxf.rt.security.SecurityConstants.*;

@Configuration
public class CxfServiceConfiguration {
    @Bean
    public Endpoint endpoint(Bus bus, DoubleItPortType service) {
        EndpointImpl endpoint = new EndpointImpl(bus, service);

        endpoint.getProperties().put(CALLBACK_HANDLER, new CallbackHandler("service"));
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "client");
        endpoint.getProperties().put(ENCRYPT_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(ENCRYPT_USERNAME, "client");
        endpoint.getProperties().put(ConfigurationConstants.DEC_PROP_FILE, "clientstore.properties");
        endpoint.getProperties().put(ConfigurationConstants.SIG_VER_PROP_FILE, "clientstore.properties");

        endpoint.publish("/double-it");
        return endpoint;
    }
}
