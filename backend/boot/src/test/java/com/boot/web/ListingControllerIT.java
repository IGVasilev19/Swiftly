package com.boot.web;

import com.boot.testsupport.Containers;
import com.jayway.jsonpath.JsonPath;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.listing.dto.ListingRequest;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ListingControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClientBase;

    WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    private String registerAndLogin(String prefix, List<Role> roles) {
        String uniqueEmail = prefix + UUID.randomUUID() + "@gmail.com";
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", prefix + " Name", "+123456789012", roles);
        
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
    void getAllListings_ShouldReturnListingsWithVehicles() {
        // 1. Register and login as OWNER
        String ownerToken = registerAndLogin("owner", List.of(Role.OWNER));

        // 2. Add a vehicle
        String uniqueVin = "1HGBH41JXMN" + UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        VehicleRequest vehicleRequest = new VehicleRequest(
                uniqueVin, "Tesla", "Model 3", "White", 2023,
                VehicleType.CAR, FuelType.ELECTRONIC, 0.0,
                List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH),
                "USA", "Palo Alto"
        );

        MultipartBodyBuilder vehicleBuilder = new MultipartBodyBuilder();
        vehicleBuilder.part("vehicleData", vehicleRequest).contentType(MediaType.APPLICATION_JSON);
        vehicleBuilder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("tesla.jpg");

        String body = new String(webTestClient.post()
                .uri("/api/v1/vehicle")
                .header("Authorization", "Bearer " + ownerToken)
                .body(BodyInserters.fromMultipartData(vehicleBuilder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody().returnResult().getResponseBody());
        Integer vehicleId = JsonPath.read(body, "$.vehicle.id");

        // 3. Create a listing
        ListingRequest listingRequest = new ListingRequest(
                new Vehicle(vehicleId),
                "Beautiful Tesla",
                "Fast and clean",
                new BigDecimal("150.00"),
                true
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + ownerToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isCreated();

        // 4. Register and login as RENTER
        String renterToken = registerAndLogin("renter", List.of(Role.RENTER));

        // 5. Get all listings
        webTestClient.get()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + renterToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(result -> System.out.println("GET ALL LISTINGS RESPONSE: " + new String(result.getResponseBody())))
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].title").isEqualTo("Beautiful Tesla")
                .jsonPath("$[0].vehicle.make").isEqualTo("Tesla")
                .jsonPath("$[0].vehicle.features").isArray()
                .jsonPath("$[0].vehicle.features.length()").isEqualTo(2);
    }
}
