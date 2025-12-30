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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthControllerIT extends Containers {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    private String extractCookie(String header, String name) {
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
    void register_ShouldPersistAndReturnCreated() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest payload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);

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
    void register_ShouldReturnBadRequestWhenUserAlreadyExists() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest payload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User already exists");
    }

    @Test
    void login_ShouldReturnAccessToken() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);
        
        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(registerPayload)
                .exchange()
                .expectStatus().isCreated();

        LogInRequest loginPayload = new LogInRequest(uniqueEmail, "@MockPassword123");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody(String.class)
                .value(token -> assertThat(token).isNotBlank());
    }

    @Test
    void login_ShouldReturnBadRequestWithWrongPassword() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);
        
        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(registerPayload)
                .exchange()
                .expectStatus().isCreated();

        LogInRequest loginPayload = new LogInRequest(uniqueEmail, "WrongPassword123");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Wrong password");
    }

    @Test
    void refresh_ShouldReturnNewAccessToken() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);
        
        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(registerPayload)
                .exchange()
                .expectStatus().isCreated();

        LogInRequest loginPayload = new LogInRequest(uniqueEmail, "@MockPassword123");
        String[] refreshTokenHolder = new String[1];
        
        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    String setCookie = result.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
                    refreshTokenHolder[0] = extractCookie(setCookie, "refresh_token");
                });

        webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .cookie("refresh_token", refreshTokenHolder[0])
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody(String.class)
                .consumeWith(result -> {
                    String body = new String(result.getResponseBody());
                    String accessToken = JsonPath.read(body, "$.accessToken");
                    assertThat(accessToken).isNotBlank();

                    String setCookie = result.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
                    assertThat(setCookie).isNotNull();
                    assertThat(setCookie).contains("refresh_token=");
                });
    }

    @Test
    void refresh_ShouldReturnUnauthorizedWhenNoCookie() {
        webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    void refresh_ShouldReturnUnauthorizedWhenInvalidToken() {
        webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .cookie("refresh_token", "invalid-token")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    void login_ShouldReturnBadRequestWhenUserNotFound() {
        LogInRequest loginPayload = new LogInRequest("nonexistent@example.com", "password123");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User not found");
    }

    @Test
    void logout_ShouldClearRefreshCookieAndInvalidateSession() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Mocking Testing Name", "+123456789012", roles);
        
        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(registerPayload)
                .exchange()
                .expectStatus().isCreated();

        LogInRequest loginPayload = new LogInRequest(uniqueEmail, "@MockPassword123");
        String[] accessTokenHolder = new String[1];
        
        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    accessTokenHolder[0] = result.getResponseBody();
                });

        webTestClient.post()
                .uri("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + accessTokenHolder[0])
                .exchange()
                .expectStatus().isNoContent()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .expectBody().isEmpty();
    }
}
