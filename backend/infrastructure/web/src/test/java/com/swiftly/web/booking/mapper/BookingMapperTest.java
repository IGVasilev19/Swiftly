package com.swiftly.web.booking.mapper;

import com.swiftly.domain.Booking;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.booking.Status;
import com.swiftly.web.booking.dto.BookingRequest;
import com.swiftly.web.booking.dto.BookingResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    @Test
    void toBooking_WithValidRequest_ShouldMapCorrectly() {
        Listing listing = new Listing(1);
        LocalDate startAt = LocalDate.now().plusDays(1);
        LocalDate endAt = LocalDate.now().plusDays(3);
        BigDecimal totalPrice = new BigDecimal("300.00");

        BookingRequest request = new BookingRequest(listing, startAt, endAt, totalPrice);

        Booking booking = BookingMapper.toBooking(request);

        assertThat(booking).isNotNull();
        assertThat(booking.getListing()).isEqualTo(listing);
        assertThat(booking.getStartAt()).isEqualTo(startAt);
        assertThat(booking.getEndAt()).isEqualTo(endAt);
        assertThat(booking.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    void toResponse_WithValidBooking_ShouldMapCorrectly() {
        Profile renter = new Profile(1, "John Doe", "+1234567890", null);
        Profile owner = new Profile(2, "Owner Name", "+0987654321", null);
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
        
        Listing listing = new Listing(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        listing.setId(1);
        
        Booking booking = new Booking(listing, renter, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), Status.REQUESTED, new BigDecimal("300.00"));
        booking.setId(1);
        booking.setCreationDate(Instant.now());

        BookingResponse response = BookingMapper.toResponse(booking);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.listing()).isNotNull();
        assertThat(response.renter()).isNotNull();
        assertThat(response.startAt()).isEqualTo(booking.getStartAt());
        assertThat(response.endAt()).isEqualTo(booking.getEndAt());
        assertThat(response.status()).isEqualTo(Status.REQUESTED);
        assertThat(response.totalPrice()).isEqualTo(new BigDecimal("300.00"));
    }
}

