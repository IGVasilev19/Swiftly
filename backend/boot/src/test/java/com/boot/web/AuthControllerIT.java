package com.boot.web;

import com.boot.testsupport.Containers;
import com.jayway.jsonpath.JsonPath;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.com.google.common.net.HttpHeaders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClientBase;

    WebTestClient webTestClient;

    private static String refreshToken;
    private static String accessToken;

    private static String extractCookie(String header, String name) {
        return Arrays.stream(header.split(";"))
                .map(String::trim)
                .filter(c -> c.startsWith(name + "="))
                .map(c -> c.substring((name + "=").length()))
                .findFirst()
                .orElseThrow();
    }

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @Order(1)
    void register_ShouldPersistAndReturnCreated() {
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest payload = new RegisterRequest("mock123@gmail.com", "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Account created successfully!");
    }

    @Test
    @Order(2)
    void login_ShouldReturnAccessToken() {
        LogInRequest payload = new LogInRequest("mock123@gmail.com", "@MockPassword123");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody(String.class)
                .consumeWith(result -> {
                    String setCookie = result.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
                    refreshToken = extractCookie(setCookie, "refresh_token");
                    String tokenBody = result.getResponseBody();
                    accessToken = tokenBody;
                })
                .value(token -> assertThat(token).isNotBlank());
    }

    @Test
    @Order(3)
    void refresh_ShouldReturnNewAccessToken() {
        webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .cookie("refresh_token", refreshToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody(String.class)
                .consumeWith(result -> {
                    String body = new String(result.getResponseBody());
                    accessToken = JsonPath.read(body, "$.accessToken");
                    
                    // Verify that the refresh token cookie is updated
                    String setCookie = result.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
                    assertThat(setCookie).isNotNull();
                    assertThat(setCookie).contains("refresh_token=");
                })
                .value(token -> assertThat(token).isNotBlank());
    }

    @Test
    @Order(4)
    void logout_ShouldClearRefreshCookieAndInvalidateSession() {
        webTestClient.post()
                .uri("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isNoContent()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody().isEmpty();
    }
}
