package org.example.double_it_sts;

import jakarta.xml.ws.BindingProvider;
import org.example.contract.doubleit.DoubleItService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class DoubleItTest {
    @Test
    void shouldDoubleNumber() {
        final var service = new DoubleItService();
        final var port = service.getDoubleItPort();
        final var requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/services/double-it");

        final var doubled = port.doubleIt(42);

        assertThat(doubled).isEqualTo(84);
    }
}
