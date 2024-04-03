package org.example.double_it_sts;

import jakarta.xml.ws.BindingProvider;
import org.apache.cxf.ws.security.trust.STSClient;
import org.example.contract.doubleit.DoubleItService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.apache.cxf.rt.security.SecurityConstants.STS_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class DoubleItTest {
    @Test
    void shouldDoubleNumber(@Autowired STSClient stsClient) {
        final var service = new DoubleItService();
        final var port = service.getDoubleItPort();
        final var requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/services/double-it");
        requestContext.put(STS_CLIENT, stsClient);

        final var doubled = port.doubleIt(42);

        assertThat(doubled).isEqualTo(84);
    }
}
