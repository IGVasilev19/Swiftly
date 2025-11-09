package com.boot.web;

import com.boot.testsupport.Containers;
import com.swiftly.boot.BootApplication;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.com.google.common.net.HttpHeaders;

import java.net.URI;

import static com.swiftly.domain.enums.user.Role.OWNER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    @WithMockUser(username = "testuser", roles = "USER")
    void register_ShouldPersistAndReturnCreated() {
        var payload = new RegisterRequest("mock123@gmail.com", "@MockPassword123", "Mocking Testing Name", "+123456789012", OWNER, "MockAddress 356", "Eindhoven", "Netherlands", "3561 CK");

        webTestClient.post()
                .uri(URI.create("http://localhost:" + port + "/api/v1/auth/register"))
                .bodyValue(payload)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Account created successfully!");
    }

    @Test
    @Order(2)
    @WithMockUser(username = "testuser", roles = "USER")
    void login_ShouldReturnAccessToken() {
        var payload = new LogInRequest("mock123@gmail.com", "@MockPassword123");

        webTestClient.post()
                .uri(URI.create("http://localhost:" + port + "/api/v1/auth/login"))
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody(String.class)
                .value(token -> assertThat(token).isNotBlank());
    }
}
