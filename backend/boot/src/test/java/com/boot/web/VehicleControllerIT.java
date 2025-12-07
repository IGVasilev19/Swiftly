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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VehicleControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClientBase;

    WebTestClient webTestClient;

    private String extractCookie(String header, String name) {
        return Arrays.stream(header.split(";"))
                .map(String::trim)
                .filter(c -> c.startsWith(name + "="))
                .map(c -> c.substring((name + "=").length()))
                .findFirst()
                .orElseThrow();
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

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void addVehicle_AsOwner_ShouldCreateVehicle() {
        String accessToken = registerAndLoginAsOwner();
        String uniqueVin = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        
        VehicleRequest vehicleRequest = new VehicleRequest(
                uniqueVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                8.5,
                List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH),
                "Netherlands",
                "Amsterdam"
        );

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("vehicleData", vehicleRequest).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");
        webTestClient.post()
                .uri("/api/v1/vehicle/add")
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Vehicle created successfully")
                .jsonPath("$.vehicle").exists()
                .jsonPath("$.vehicle.vin").isEqualTo(uniqueVin);
    }

    @Test
    void addVehicle_AsRenter_ShouldReturnForbidden() {
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

        String uniqueVin = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
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
        builder.part("vehicleData", vehicleRequest).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");

        webTestClient.post()
                .uri("/api/v1/vehicle/add")
                .header("Authorization", "Bearer " + accessTokenHolder[0])
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void addVehicle_Unauthenticated_ShouldReturnUnauthorized() {
        String uniqueVin = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
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
        builder.part("vehicleData", vehicleRequest).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");
        webTestClient.post()
                .uri("/api/v1/vehicle/add")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getOwnedVehicles_AsOwner_ShouldReturnOwnedVehicles() {
        String accessToken = registerAndLoginAsOwner();

        String uniqueVin1 = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        VehicleRequest vehicleRequest1 = new VehicleRequest(
                uniqueVin1,
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

        MultipartBodyBuilder builder1 = new MultipartBodyBuilder();
        builder1.part("vehicleData", vehicleRequest1).contentType(MediaType.APPLICATION_JSON);
        builder1.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");

        webTestClient.post()
                .uri("/api/v1/vehicle/add")
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder1.build()))
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri("/api/v1/vehicle/owned")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].vin").isEqualTo(uniqueVin1);
    }

    @Test
    void getOwnedVehicles_NoVehicles_ShouldReturnEmptyArray() {
        String accessToken = registerAndLoginAsOwner();

        webTestClient.get()
                .uri("/api/v1/vehicle/owned")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isEmpty();
    }

    @Test
    void getVehicle_ExistingVehicle_ShouldReturnVehicle() {
        String accessToken = registerAndLoginAsOwner();
        
        String uniqueVin = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
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
        builder.part("vehicleData", vehicleRequest).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");

        String[] vehicleIdHolder = new String[1];
        
        webTestClient.post()
                .uri("/api/v1/vehicle/add")
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(result -> {
                    String body = new String(result.getResponseBody());
                    vehicleIdHolder[0] = JsonPath.read(body, "$.vehicle.id").toString();
                });

        webTestClient.get()
                .uri("/api/v1/vehicle/" + vehicleIdHolder[0])
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.vin").isEqualTo(uniqueVin)
                .jsonPath("$.make").isEqualTo("Toyota")
                .jsonPath("$.model").isEqualTo("Camry");
    }

    @Test
    void getVehicle_NonExistentVehicle_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();

        webTestClient.get()
                .uri("/api/v1/vehicle/99999")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isBadRequest();
    }
}

