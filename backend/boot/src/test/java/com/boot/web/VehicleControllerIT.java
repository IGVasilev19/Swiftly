package com.boot.web;

import com.boot.testsupport.Containers;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.profile.JpaProfileRepository;
import com.swiftly.persistence.user.JpaUserRepository;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.vehicle.dto.VehicleRequest;
import com.swiftly.web.vehicle.dto.VehicleUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VehicleControllerIT extends Containers {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaProfileRepository profileRepository;

    @Autowired
    JpaVehicleRepository vehicleRepository;

    WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
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

    private String registerAndLoginAsOwnerWithEmail(String[] emailHolder) {
        String uniqueEmail = "owner" + UUID.randomUUID() + "@gmail.com";
        emailHolder[0] = uniqueEmail;
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

    private Integer getOwnerIdFromEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getProfile().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    @Test
    void addVehicle_AsOwner_ShouldReturnCreated() throws Exception {
        String accessToken = registerAndLoginAsOwner();
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
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Vehicle created successfully")
                .jsonPath("$.vehicle.vin").isEqualTo(uniqueVin)
                .jsonPath("$.vehicle.make").isEqualTo("Toyota")
                .jsonPath("$.vehicle.model").isEqualTo("Camry");
    }

    @Test
    void getOwnedVehicles_WithVehicles_ShouldReturnOk() throws Exception {
        String[] emailHolder = new String[1];
        String accessToken = registerAndLoginAsOwnerWithEmail(emailHolder);

        Integer ownerId = getOwnerIdFromEmail(emailHolder[0]);
        ProfileEntity owner = profileRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        VehicleEntity vehicle = new VehicleEntity(
                owner,
                generateValidVin(),
                "Tesla",
                "Model 3",
                "White",
                2023,
                VehicleType.CAR,
                FuelType.ELECTRONIC,
                0.1,
                List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH),
                "USA",
                "Palo Alto"
        );
        vehicleRepository.save(vehicle);

        webTestClient.get()
                .uri("/api/v1/vehicle/owned")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].make").isEqualTo("Tesla")
                .jsonPath("$[0].model").isEqualTo("Model 3");
    }

    @Test
    void getVehicle_ExistingVehicle_ShouldReturnOk() throws Exception {
        String[] emailHolder = new String[1];
        String accessToken = registerAndLoginAsOwnerWithEmail(emailHolder);

        Integer ownerId = getOwnerIdFromEmail(emailHolder[0]);
        ProfileEntity owner = profileRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        String uniqueVin = generateValidVin();
        VehicleEntity vehicle = new VehicleEntity(
                owner,
                uniqueVin,
                "BMW",
                "X5",
                "Black",
                2022,
                VehicleType.SUV,
                FuelType.DIESEL,
                7.5,
                List.of(Feature.AIR_CONDITIONING, Feature.NAVIGATION),
                "Germany",
                "Munich"
        );
        VehicleEntity savedVehicle = vehicleRepository.save(vehicle);

        webTestClient.get()
                .uri("/api/v1/vehicle/" + savedVehicle.getId())
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedVehicle.getId())
                .jsonPath("$.vin").isEqualTo(uniqueVin)
                .jsonPath("$.make").isEqualTo("BMW")
                .jsonPath("$.model").isEqualTo("X5");
    }

    @Test
    void updateVehicle_AsOwner_ShouldReturnOk() throws Exception {
        String[] emailHolder = new String[1];
        String accessToken = registerAndLoginAsOwnerWithEmail(emailHolder);

        Integer ownerId = getOwnerIdFromEmail(emailHolder[0]);
        ProfileEntity owner = profileRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        VehicleEntity vehicle = new VehicleEntity(
                owner,
                generateValidVin(),
                "Honda",
                "Civic",
                "Red",
                2021,
                VehicleType.CAR,
                FuelType.PETROL,
                6.5,
                List.of(Feature.AIR_CONDITIONING),
                "Japan",
                "Tokyo"
        );
        VehicleEntity savedVehicle = vehicleRepository.save(vehicle);

        String updatedVin = generateValidVin();
        VehicleUpdateRequest updateRequest = new VehicleUpdateRequest(
                updatedVin,
                "Honda",
                "Accord",
                "Blue",
                2022,
                VehicleType.CAR,
                FuelType.HYBRID,
                5.5,
                List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH),
                "Japan",
                "Osaka"
        );

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("vehicleData", objectMapper.writeValueAsString(updateRequest)).contentType(MediaType.APPLICATION_JSON);

        webTestClient.put()
                .uri("/api/v1/vehicle/" + savedVehicle.getId())
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Vehicle updated successfully");
    }

    @Test
    void deleteVehicle_AsOwner_ShouldReturnOk() throws Exception {
        String[] emailHolder = new String[1];
        String accessToken = registerAndLoginAsOwnerWithEmail(emailHolder);

        Integer ownerId = getOwnerIdFromEmail(emailHolder[0]);
        ProfileEntity owner = profileRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        VehicleEntity vehicle = new VehicleEntity(
                owner,
                generateValidVin(),
                "Ford",
                "Fiesta",
                "Green",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                5.0,
                List.of(Feature.AIR_CONDITIONING),
                "USA",
                "Detroit"
        );
        VehicleEntity savedVehicle = vehicleRepository.save(vehicle);

        webTestClient.delete()
                .uri("/api/v1/vehicle/" + savedVehicle.getId())
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Vehicle deleted successfully");
    }
}

