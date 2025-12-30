package com.boot.web;

import com.boot.testsupport.Containers;
import com.jayway.jsonpath.JsonPath;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.vehicle.dto.VehicleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VehicleControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    private String generateValidVin() {
        String base = "1HGBH41JXMN";
        String validChars = "ABCDEFGHJKLMNPRTUVWXYZ0123456789";
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * validChars.length());
            suffix.append(validChars.charAt(index));
        }
        String vin = base + suffix.toString();
        assert vin.length() == 17 : "VIN must be exactly 17 characters, got: " + vin.length();
        return vin;
    }

    private String registerAndLoginAsOwner() {
        String uniqueEmail = "owner" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Owner Name", "+123456789012", roles);
        
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

        return accessTokenHolder[0];
    }


    @Test
    void addVehicle_AsRenter_ShouldReturnForbidden() throws Exception {
        String uniqueEmail = "renter" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.RENTER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "Renter Name", "+123456789012", roles);
        
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

        String uniqueVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                uniqueVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                8.5,
                List.of(Feature.AIR_CONDITIONING),
                "Netherlands",
                "Amsterdam"
        );

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("vehicleData", objectMapper.writeValueAsString(vehicleRequest)).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");

        webTestClient.post()
                .uri("/api/v1/vehicle")
                .header("Authorization", "Bearer " + accessTokenHolder[0])
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void addVehicle_Unauthenticated_ShouldReturnForbidden() throws Exception {
        String uniqueVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                uniqueVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                8.5,
                List.of(Feature.AIR_CONDITIONING),
                "Netherlands",
                "Amsterdam"
        );

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("vehicleData", objectMapper.writeValueAsString(vehicleRequest)).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");
        webTestClient.post()
                .uri("/api/v1/vehicle")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    void getOwnedVehicles_NoVehicles_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();

        webTestClient.get()
                .uri("/api/v1/vehicle/owned")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").exists();
    }


    @Test
    void getVehicle_NonExistentVehicle_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();

        webTestClient.get()
                .uri("/api/v1/vehicle/99999")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isBadRequest();
    }
}

