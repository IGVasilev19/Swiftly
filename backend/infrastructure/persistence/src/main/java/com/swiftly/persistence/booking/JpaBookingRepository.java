package com.swiftly.persistence.booking;

import com.swiftly.domain.Booking;
import com.swiftly.persistence.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, Integer> {
    BookingEntity findByListingId(Integer listingId);
    BookingEntity findByRenterId(Integer renterId);
    List<BookingEntity> findAllByRenterId(Integer renterId);
    List<BookingEntity> findAllByListingId(Integer listingId);
    Boolean existsByListingIdAndStartDateLessThanAndEndDateGreaterThan(Integer listingId, Instant end, Instant start);
}
