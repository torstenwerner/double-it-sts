package org.example.double_it_sts;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.endpoint.EndpointException;
import org.apache.cxf.ws.security.trust.STSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
public class CxfClientConfiguration {

    @Bean
    public STSClient stsClient(Bus bus) throws EndpointException, BusException {
        final var stsClient = new STSClient(bus);
//        stsClient.setSoap12();
//        stsClient.getRequestContext().put("soap.no.validate.parts", "true");
//        stsClient.setWsdlLocation("/wsdl/IdProviderX509Certificate.wsdl");
//        stsClient.setEndpointQName(QName.valueOf("{http://idp.safe.de/}IIdProviderService_X509CertificatePort"));
//        stsClient.setServiceQName(QName.valueOf("{http://idp.safe.de/}IdProviderService_X509Certificate"));
        stsClient.setLocation("http://localhost:8080/services/sts");
//        stsClient.getClient().getConduit().getTarget().getAddress().setValue("http://localhost:8080/services/sts");
        return stsClient;
    }
}
