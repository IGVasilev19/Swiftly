package com.swiftly.application.booking.port.outbound;

import com.swiftly.domain.Booking;

import java.time.Instant;
import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    Booking findById(Integer id);
    Booking findByListingId(Integer listingId);
    Booking findByRenterId(Integer renterId);
    List<Booking> findAll();
    List<Booking> findAllByRenterId(Integer renterId);
    List<Booking> findAllByListingId(Integer listingId);
    Boolean existsByListingIdAndStartDateLessThanAndEndDateGreaterThan(Integer listingId, Instant end, Instant start);
}
