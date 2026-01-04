package com.boot.web;

import com.boot.testsupport.Containers;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EnumControllerIT extends Containers {

    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .build();
    }

    private String registerAndLogin() {
        String uniqueEmail = "user" + UUID.randomUUID() + "@gmail.com";
        List<Role> roles = List.of(Role.OWNER);
        RegisterRequest registerPayload = new RegisterRequest(uniqueEmail, "@MockPassword123", "User Name", "+123456789012", roles);
        
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
    void vehicleTypes_ShouldReturnAllVehicleTypes() {
        String accessToken = registerAndLogin();
        webTestClient.get()
                .uri("/api/v1/enums/vehicle-types")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleType[].class)
                .value(types -> {
                    assertThat(types).isNotNull();
                    assertThat(types.length).isGreaterThan(0);
                    boolean hasCar = false;
                    for (VehicleType type : types) {
                        if (type == VehicleType.CAR) {
                            hasCar = true;
                            break;
                        }
                    }
                    assertThat(hasCar).isTrue();
                });
    }

    @Test
    void fuelTypes_ShouldReturnAllFuelTypes() {
        String accessToken = registerAndLogin();
        webTestClient.get()
                .uri("/api/v1/enums/fuel-types")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuelType[].class)
                .value(types -> {
                    assertThat(types).isNotNull();
                    assertThat(types.length).isGreaterThan(0);
                    boolean hasPetrol = false;
                    for (FuelType type : types) {
                        if (type == FuelType.PETROL) {
                            hasPetrol = true;
                            break;
                        }
                    }
                    assertThat(hasPetrol).isTrue();
                });
    }

    @Test
    void features_ShouldReturnAllFeatures() {
        String accessToken = registerAndLogin();
        webTestClient.get()
                .uri("/api/v1/enums/features")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Feature[].class)
                .value(features -> {
                    assertThat(features).isNotNull();
                    assertThat(features.length).isGreaterThan(0);
                    boolean hasAC = false;
                    for (Feature feature : features) {
                        if (feature == Feature.AIR_CONDITIONING) {
                            hasAC = true;
                            break;
                        }
                    }
                    assertThat(hasAC).isTrue();
                });
    }
}

