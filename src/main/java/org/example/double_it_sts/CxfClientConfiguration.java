package org.example.double_it_sts;

import jakarta.xml.ws.BindingProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.trust.STSClient;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.example.contract.doubleit.DoubleItPortType;
import org.example.contract.doubleit.DoubleItService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

import static org.apache.cxf.rt.security.SecurityConstants.*;

@Configuration
public class CxfClientConfiguration {

    private final LoggingFeature loggingFeature = new LoggingFeature();

    @Bean
    public STSClient stsClient(Bus bus, CleartextLogger cleartextLogger) {
        final var stsClient = new STSClient(bus);
        stsClient.setWsdlLocation("DoubleSTSService.wsdl");
        stsClient.setServiceQName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"));
        stsClient.setEndpointQName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}STS_Port"));

        stsClient.getProperties().put(USERNAME, "alice");
        stsClient.getProperties().put(CALLBACK_HANDLER, new ClientCallbackHandler());
        stsClient.getProperties().put(ENCRYPT_PROPERTIES, "clientstore.properties");
        stsClient.getProperties().put(ENCRYPT_USERNAME, "client");
        stsClient.getProperties().put(SIGNATURE_PROPERTIES, "clientstore.properties");
        stsClient.getProperties().put(SIGNATURE_USERNAME, "client");
        stsClient.getProperties().put(STS_TOKEN_PROPERTIES, "clientstore.properties");
        stsClient.getProperties().put(STS_TOKEN_USERNAME, "client");

        stsClient.setFeatures(List.of(loggingFeature));
        stsClient.getInInterceptors().add(cleartextLogger);
        stsClient.getOutInterceptors().add(cleartextLogger);

        return stsClient;
    }

    @Bean("client")
    public DoubleItPortType client(STSClient stsClient, CleartextLogger cleartextLogger) {
        final var service = new DoubleItService(loggingFeature);
        final var port = service.getDoubleItPort();

        final var client = ClientProxy.getClient(port);
        client.getInInterceptors().add(cleartextLogger);
        client.getOutInterceptors().add(cleartextLogger);

        final Map<String, Object> inProps = Map.of(
                WSHandlerConstants.ACTION, WSHandlerConstants.SAML_TOKEN_UNSIGNED,
                WSHandlerConstants.SAML_CALLBACK_REF, new ClientCallbackHandler());
        client.getInInterceptors().add(new WSS4JInInterceptor(inProps));
        final Map<String, Object> outProps = Map.of(
                WSHandlerConstants.ACTION, WSHandlerConstants.SAML_TOKEN_UNSIGNED,
                WSHandlerConstants.SAML_CALLBACK_REF, new ClientCallbackHandler());
        client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));

        final var requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/services/double-it");
        requestContext.put(STS_CLIENT, stsClient);
        requestContext.put(SAML_CALLBACK_HANDLER, new ClientCallbackHandler());

        return port;
    }
}
