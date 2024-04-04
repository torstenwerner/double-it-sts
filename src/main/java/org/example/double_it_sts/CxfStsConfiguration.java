package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import lombok.SneakyThrows;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.sts.StaticSTSProperties;
import org.apache.cxf.sts.provider.DefaultSecurityTokenServiceProvider;
import org.apache.cxf.sts.service.StaticService;
import org.apache.cxf.ws.security.sts.provider.SecurityTokenServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.util.List;

import static org.apache.cxf.rt.security.SecurityConstants.*;

/**
 * Configures the STS.
 */
@Configuration
public class CxfStsConfiguration {
    /**
     * Creates the SAML token provider.
     */
    @SneakyThrows
    @Bean
    public SecurityTokenServiceProvider securityTokenServiceProvider() {
        final var provider = new DefaultSecurityTokenServiceProvider();

        final var stsProperties = new StaticSTSProperties();
        stsProperties.setIssuer("http://localhost:8080/services/sts");
        stsProperties.setCallbackHandler(new CallbackHandler("sts-provider"));
        stsProperties.setEncryptionCryptoProperties("stsstore.properties");
        stsProperties.setEncryptionUsername("server");
        stsProperties.setSignatureCryptoProperties("stsstore.properties");
        stsProperties.setSignatureUsername("sts");
        provider.setStsProperties(stsProperties);

        final var service = new StaticService();
        service.setEndpoints(List.of("http://localhost:8080/services/double-it"));
        provider.setServices(List.of(service));
        return provider;
    }

    /**
     * Creates the endpoint for the STS.
     */
    @Bean
    public Endpoint sts(Bus bus, SecurityTokenServiceProvider stsProvider) {
        final var endpoint = new EndpointImpl(bus, stsProvider);
        endpoint.setWsdlLocation("DoubleSTSService.wsdl");
        endpoint.setServiceName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"));
        endpoint.setEndpointName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}STS_Port"));
        endpoint.setPublishedEndpointUrl("http://localhost:8080/services/sts");

        endpoint.getProperties().put(CALLBACK_HANDLER, new CallbackHandler("sts-service"));
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "stsstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "sts");
        endpoint.getProperties().put(ENCRYPT_PROPERTIES, "stsstore.properties");
        endpoint.getProperties().put(ENCRYPT_USERNAME, "user"); // ???

        endpoint.publish("/sts");
        return endpoint;
    }
}
