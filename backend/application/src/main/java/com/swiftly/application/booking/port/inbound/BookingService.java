package com.swiftly.application.booking.port.inbound;

import com.swiftly.domain.Booking;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface BookingService {
    Booking create(Booking booking);
    Booking getById(Integer id);
    Booking getByListingId(Integer listingId);
    Booking getByRenterId(Integer renterId);
    List<Booking> getAll();
    List<Booking> getAllByRenterId(Integer renterId);
    List<Booking> getAllByListingId(Integer listingId);
    Boolean bookingAlreadyExists(Integer listingId, Instant end, Instant start);
    void calculateTotalPrice(Booking booking);
}
