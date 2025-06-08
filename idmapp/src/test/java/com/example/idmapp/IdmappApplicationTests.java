package com.example.idmapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.example.idmapp.config.TestSecurityConfig;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "auth0.domain=test-tenant.auth0.com",
    "auth0.audience=https://test-api.your-domain.com",
    "auth0.client-id=test-client-id",
    "auth0.client-secret=test-client-secret"
})
class IdmappApplicationTests {

	@Test
	void contextLoads() {
	}

}
