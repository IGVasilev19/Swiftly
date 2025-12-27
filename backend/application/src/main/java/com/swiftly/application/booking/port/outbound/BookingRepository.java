package com.swiftly.application.booking.port.outbound;

import com.swiftly.domain.Booking;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    Booking findById(Integer id);
    List<Booking> findAllByListingVehicleOwnerId(Integer ownerId);
    List<Booking> findAll();
    List<Booking> findAllByRenterId(Integer renterId);
    List<Booking> findAllByListingId(Integer listingId);
    Boolean existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(Integer listingId, LocalDate endAt, LocalDate  startAt);
}
