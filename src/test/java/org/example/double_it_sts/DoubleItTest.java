package org.example.double_it_sts;

import org.example.contract.doubleit.DoubleItPortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class DoubleItTest {
    @Test
    void shouldDoubleNumber(@Autowired DoubleItPortType client) {
        final var doubled = client.doubleIt(42);

        assertThat(doubled).isEqualTo(84);
    }
}
