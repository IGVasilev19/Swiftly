package com.swiftly.application.booking.port.inbound;

import com.swiftly.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking create(Booking booking);
    Booking getById(Integer id);
    List<Booking> getAllByRenterId(Integer renterId);
    List<Booking> getAllByListingId(Integer listingId);
    Boolean bookingAlreadyExists(Integer listingId, LocalDate endAt, LocalDate  startAt);
    List<Booking> getAllByListingVehicleOwnerId(Integer ownerId);
}
