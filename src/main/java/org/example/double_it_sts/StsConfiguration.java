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

@Configuration
public class StsConfiguration {
    @SneakyThrows
    @Bean
    public SecurityTokenServiceProvider securityTokenServiceProvider() {
        final var provider = new DefaultSecurityTokenServiceProvider();
        
        final var stsProperties = new StaticSTSProperties();
        stsProperties.setSignatureUsername("signatureUsername");
        stsProperties.setIssuer("issuer");
//        stsProperties.setCallbackHandler(callbacks -> {});
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
        endpoint.publish("/sts"); // http://localhost:8080/services/sts?wsdl
        return endpoint;
    }
}
