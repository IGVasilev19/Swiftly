package com.boot.web;

import com.boot.testsupport.Containers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.Listing;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.entities.*;
import com.swiftly.persistence.listing.JpaListingRepository;
import com.swiftly.persistence.profile.JpaProfileRepository;
import com.swiftly.persistence.user.JpaUserRepository;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.booking.dto.BookingRequest;
import com.swiftly.web.listing.dto.ListingRequest;
import com.swiftly.web.vehicle.dto.VehicleRequest;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ValidationIT extends Containers {

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

    @Autowired
    JpaListingRepository listingRepository;

    WebTestClient webTestClient;

    @BeforeEach
    void setupClient() {
        this.webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .build();
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

    private String registerAndLoginAsRenter() {
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

        return accessTokenHolder[0];
    }

    private Integer getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private VehicleEntity setupTestVehicle(Integer ownerId) {
        ProfileEntity owner = profileRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        VehicleEntity vehicle = new VehicleEntity(
                owner,
                "1HGBH41JXMN109876",
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
        return vehicleRepository.save(vehicle);
    }

    private ListingEntity setupTestListing(VehicleEntity vehicle) {
        ListingEntity listing = new ListingEntity(
                vehicle,
                "Beautiful Tesla",
                "Fast and clean",
                new BigDecimal("150.00"),
                true
        );
        return listingRepository.save(listing);
    }

    private String generateValidVin() {
        String base = "1HGBH41JXMN";
        String validChars = "ABCDEFGHJKLMNPRTUVWXYZ0123456789";
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * validChars.length());
            suffix.append(validChars.charAt(index));
        }
        return base + suffix.toString();
    }

    @Test
    void register_WithBlankEmail_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("", "@MockPassword123", "Name", "+123456789012", List.of(Role.OWNER));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void register_WithInvalidEmail_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("invalid-email", "@MockPassword123", "Name", "+123456789012", List.of(Role.OWNER));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void register_WithBlankPassword_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("test@example.com", "", "Name", "+123456789012", List.of(Role.OWNER));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void register_WithBlankName_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("test@example.com", "@MockPassword123", "", "+123456789012", List.of(Role.OWNER));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void register_WithInvalidPhoneNumber_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("test@example.com", "@MockPassword123", "Name", "123", List.of(Role.OWNER));

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void register_WithNullRoles_ShouldReturnBadRequest() {
        RegisterRequest payload = new RegisterRequest("test@example.com", "@MockPassword123", "Name", "+123456789012", null);

        webTestClient.post()
                .uri("/api/v1/auth/register")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void login_WithBlankEmail_ShouldReturnBadRequest() {
        LogInRequest payload = new LogInRequest("", "@MockPassword123");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void login_WithBlankPassword_ShouldReturnBadRequest() {
        LogInRequest payload = new LogInRequest("test@example.com", "");

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankVin_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        VehicleRequest vehicleRequest = new VehicleRequest(
                "",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithInvalidVinPattern_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        VehicleRequest vehicleRequest = new VehicleRequest(
                "INVALID-VIN-12345",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankMake_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankModel_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankColor_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithNullYear_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                null,
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithNullType_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                null,
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithNullFuelType_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                null,
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithNullFuelConsumption_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                null,
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithFuelConsumptionBelowMinimum_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                0.05,
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankCountry_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                8.5,
                List.of(Feature.AIR_CONDITIONING),
                "",
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
                .expectStatus().isBadRequest();
    }

    @Test
    void addVehicle_WithBlankCity_ShouldReturnBadRequest() throws Exception {
        String accessToken = registerAndLoginAsOwner();
        String validVin = generateValidVin();
        VehicleRequest vehicleRequest = new VehicleRequest(
                validVin,
                "Toyota",
                "Camry",
                "Blue",
                2020,
                VehicleType.CAR,
                FuelType.PETROL,
                8.5,
                List.of(Feature.AIR_CONDITIONING),
                "Netherlands",
                ""
        );

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("vehicleData", objectMapper.writeValueAsString(vehicleRequest)).contentType(MediaType.APPLICATION_JSON);
        builder.part("images", "fake-image-data".getBytes()).contentType(MediaType.IMAGE_JPEG).filename("test.jpg");

        webTestClient.post()
                .uri("/api/v1/vehicle")
                .header("Authorization", "Bearer " + accessToken)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addListing_WithNullVehicle_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();
        ListingRequest listingRequest = new ListingRequest(
                null,
                "Test Listing",
                "Test Description",
                new BigDecimal("100.00"),
                true
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addListing_WithBlankTitle_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();
        ListingRequest listingRequest = new ListingRequest(
                new Vehicle(1),
                "",
                "Test Description",
                new BigDecimal("100.00"),
                true
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addListing_WithBlankDescription_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();
        ListingRequest listingRequest = new ListingRequest(
                new Vehicle(1),
                "Test Listing",
                "",
                new BigDecimal("100.00"),
                true
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addListing_WithNullBasePricePerDay_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();
        ListingRequest listingRequest = new ListingRequest(
                new Vehicle(1),
                "Test Listing",
                "Test Description",
                null,
                true
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addListing_WithNullInstantBook_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsOwner();
        ListingRequest listingRequest = new ListingRequest(
                new Vehicle(1),
                "Test Listing",
                "Test Description",
                new BigDecimal("100.00"),
                null
        );

        webTestClient.post()
                .uri("/api/v1/listing")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(listingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBooking_WithNullListing_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsRenter();
        BookingRequest bookingRequest = new BookingRequest(
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                new BigDecimal("300.00")
        );

        webTestClient.post()
                .uri("/api/v1/booking")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBooking_WithNullStartAt_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsRenter();
        BookingRequest bookingRequest = new BookingRequest(
                new Listing(1),
                null,
                LocalDate.now().plusDays(3),
                new BigDecimal("300.00")
        );

        webTestClient.post()
                .uri("/api/v1/booking")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBooking_WithNullEndAt_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsRenter();
        BookingRequest bookingRequest = new BookingRequest(
                new Listing(1),
                LocalDate.now().plusDays(1),
                null,
                new BigDecimal("300.00")
        );

        webTestClient.post()
                .uri("/api/v1/booking")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBooking_WithNullTotalPrice_ShouldReturnBadRequest() {
        String accessToken = registerAndLoginAsRenter();
        BookingRequest bookingRequest = new BookingRequest(
                new Listing(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                null
        );

        webTestClient.post()
                .uri("/api/v1/booking")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}

