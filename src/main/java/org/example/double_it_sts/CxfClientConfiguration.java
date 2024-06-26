package org.example.double_it_sts;

import jakarta.xml.ws.BindingProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.trust.STSClient;
import org.example.contract.doubleit.DoubleItPortType;
import org.example.contract.doubleit.DoubleItService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.util.List;

import static org.apache.cxf.rt.security.SecurityConstants.*;

/**
 * Configures the client side and the STS client.
 */
@Configuration
public class CxfClientConfiguration {

    private final LoggingFeature loggingFeature = new LoggingFeature();

    /**
     * Creates the STS client needed for authentication.
     */
    @Bean
    public STSClient stsClient(Bus bus, CleartextLogger cleartextLogger) {
        final var stsClient = new STSClient(bus);
        stsClient.setWsdlLocation("DoubleSTSService.wsdl");
        stsClient.setServiceQName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"));
        stsClient.setEndpointQName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}STS_Port"));

        stsClient.getProperties().put(USERNAME, "alice");
        stsClient.getProperties().put(CALLBACK_HANDLER, new CallbackHandler("sts-client"));
        // username + password based authentication
//        stsClient.getProperties().put(ENCRYPT_PROPERTIES, "clientstore.properties");
        // certificate based authentication
        stsClient.getProperties().put(ENCRYPT_PROPERTIES, "userstore.properties");
        stsClient.getProperties().put(ENCRYPT_USERNAME, "sts");

        stsClient.setFeatures(List.of(loggingFeature));
        stsClient.getInInterceptors().add(cleartextLogger);
        stsClient.getOutInterceptors().add(cleartextLogger);

        return stsClient;
    }

    /**
     * Creates the webservice client.
     */
    @Bean("client")
    public DoubleItPortType client(STSClient stsClient, CleartextLogger cleartextLogger) {
        final var service = new DoubleItService(loggingFeature);
        final var port = service.getDoubleItPort();

        final var client = ClientProxy.getClient(port);
        client.getInInterceptors().add(cleartextLogger);
        client.getOutInterceptors().add(cleartextLogger);

        final var requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/services/double-it");
        requestContext.put(STS_CLIENT, stsClient);
        // certificate based authentication
        requestContext.put(SIGNATURE_PROPERTIES, "userstore.properties");
        requestContext.put(SIGNATURE_USERNAME, "user");

        return port;
    }
}
