package org.example.double_it_sts;

import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.soap.SOAPBinding;
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

@Configuration
public class CxfStsConfiguration {
    @SneakyThrows
    @Bean
    public SecurityTokenServiceProvider securityTokenServiceProvider() {
        final var provider = new DefaultSecurityTokenServiceProvider();
        
        final var stsProperties = new StaticSTSProperties();
        stsProperties.setCallbackHandler(new ClientCallbackHandler());
        stsProperties.setEncryptionCryptoProperties("clientstore.properties");
        stsProperties.setEncryptionUsername("client");
        stsProperties.setSignatureCryptoProperties("clientstore.properties");
        stsProperties.setSignatureUsername("client");
        stsProperties.configureProperties();
        provider.setStsProperties(stsProperties);
        
        final var service = new StaticService();
        service.setEndpoints(List.of("http://localhost:8080/services/double-it"));
        provider.setServices(List.of(service));
        return provider;
    }

    @Bean
    public Endpoint sts(Bus bus, SecurityTokenServiceProvider stsProvider) {
        final var endpoint = new EndpointImpl(bus, stsProvider);
        endpoint.setWsdlLocation("DoubleSTSService.wsdl");
        endpoint.setServiceName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"));
        endpoint.setEndpointName(QName.valueOf("{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}STS_Port"));
        endpoint.setBindingUri(SOAPBinding.SOAP12HTTP_BINDING);
        endpoint.setPublishedEndpointUrl("http://localhost:8080/services/sts");

        endpoint.getProperties().put(CALLBACK_HANDLER, new ClientCallbackHandler());
        endpoint.getProperties().put(SIGNATURE_PROPERTIES, "clientstore.properties");
        endpoint.getProperties().put(SIGNATURE_USERNAME, "client");

        endpoint.publish("/sts");
        return endpoint;
    }
}
