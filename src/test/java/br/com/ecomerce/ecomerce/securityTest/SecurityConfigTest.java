package br.com.ecomerce.ecomerce.securityTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void shouldAllowPublicGetEndpoints() {
        ResponseEntity<String> response =
                rest.getForEntity("/produtos", String.class);

        assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldBlockProtectedEndpoint() {
        ResponseEntity<String> response =
                rest.getForEntity("/clientes/1", String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldAllowH2Console() {
        ResponseEntity<String> response =
                rest.getForEntity("/h2-console", String.class);

        assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}