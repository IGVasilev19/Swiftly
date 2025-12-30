package com.swiftly.web.listing.mapper;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import com.swiftly.web.listing.dto.ListingRequest;
import com.swiftly.web.listing.dto.ListingResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ListingMapperTest {

    @Test
    void toListing_WithValidRequest_ShouldMapCorrectly() {
        Vehicle vehicle = new Vehicle(1);
        String title = "Beautiful Car";
        String description = "Fast and clean";
        BigDecimal basePricePerDay = new BigDecimal("150.00");
        Boolean instantBook = true;

        ListingRequest request = new ListingRequest(vehicle, title, description, basePricePerDay, instantBook);

        Listing listing = ListingMapper.toListing(request);

        assertThat(listing).isNotNull();
        assertThat(listing.getVehicle()).isEqualTo(vehicle);
        assertThat(listing.getTitle()).isEqualTo(title);
        assertThat(listing.getDescription()).isEqualTo(description);
        assertThat(listing.getBasePricePerDay()).isEqualTo(basePricePerDay);
        assertThat(listing.getInstantBook()).isEqualTo(instantBook);
    }

    @Test
    void toResponse_WithValidListing_ShouldMapCorrectly() {
        com.swiftly.domain.Profile owner = new com.swiftly.domain.Profile(1, "Owner Name", "+1234567890", null);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setOwner(owner);
        vehicle.setVin("1HGBH41JXMN12345");
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setColor("Blue");
        vehicle.setYear(2020);
        vehicle.setFuelConsumption(8.5);
        vehicle.setCountry("Netherlands");
        vehicle.setCity("Amsterdam");
        
        Listing listing = new Listing(vehicle, "Beautiful Car", "Fast and clean", new BigDecimal("150.00"), true);
        listing.setId(1);
        listing.setCreationDate(Instant.now());

        ListingResponse response = ListingMapper.toResponse(listing);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.title()).isEqualTo("Beautiful Car");
        assertThat(response.description()).isEqualTo("Fast and clean");
        assertThat(response.basePricePerDay()).isEqualTo(new BigDecimal("150.00"));
        assertThat(response.instantBook()).isTrue();
        assertThat(response.vehicle()).isNotNull();
    }
}

