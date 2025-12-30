package com.boot.web;

import com.boot.testsupport.Containers;
import com.swiftly.boot.BootApplication;
import com.swiftly.domain.enums.booking.Status;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.entities.*;
import com.swiftly.persistence.booking.JpaBookingRepository;
import com.swiftly.persistence.listing.JpaListingRepository;
import com.swiftly.persistence.profile.JpaProfileRepository;
import com.swiftly.persistence.user.JpaUserRepository;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.booking.dto.BookingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerIT extends Containers {

    @LocalServerPort
    int port;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaProfileRepository profileRepository;

    @Autowired
    JpaVehicleRepository vehicleRepository;

    @Autowired
    JpaListingRepository listingRepository;

    @Autowired
    JpaBookingRepository bookingRepository;

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

    private BookingEntity setupTestBooking(ListingEntity listing, Integer renterId) {
        ProfileEntity renter = profileRepository.findById(renterId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        BookingEntity booking = new BookingEntity(
                listing,
                renter,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                Status.REQUESTED,
                new BigDecimal("300.00")
        );
        return bookingRepository.save(booking);
    }




    @Test
    void createBooking_Unauthenticated_ShouldReturnForbidden() throws Exception {
        BookingRequest bookingRequest = new BookingRequest(
                new com.swiftly.domain.Listing(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                new BigDecimal("300.00")
        );

        webTestClient.post()
                .uri("/api/v1/booking")
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    void getAllRenterBookings_AsOwner_ShouldReturnForbidden() throws Exception {
        String ownerToken = registerAndLogin("owner", List.of(Role.OWNER));

        webTestClient.get()
                .uri("/api/v1/booking/me")
                .header("Authorization", "Bearer " + ownerToken)
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    void getAllOwnerBookings_AsRenter_ShouldReturnForbidden() throws Exception {
        String renterToken = registerAndLogin("renter", List.of(Role.RENTER));

        webTestClient.get()
                .uri("/api/v1/booking/owned")
                .header("Authorization", "Bearer " + renterToken)
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    void getBooking_NonExistentBooking_ShouldReturnBadRequest() throws Exception {
        String renterToken = registerAndLogin("renter", List.of(Role.RENTER));

        webTestClient.get()
                .uri("/api/v1/booking/99999")
                .header("Authorization", "Bearer " + renterToken)
                .exchange()
                .expectStatus().isBadRequest();
    }


}

