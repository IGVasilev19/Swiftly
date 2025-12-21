package com.swiftly.persistence.booking;

import com.swiftly.domain.Booking;
import com.swiftly.persistence.entities.BookingEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, Integer> {
    @EntityGraph(attributePaths = {"listing", "listing.vehicle"})
    Optional<BookingEntity> findById(Integer id);
    @EntityGraph(attributePaths = {"listing", "listing.vehicle"})
    BookingEntity findByListingId(Integer listingId);
    @EntityGraph(attributePaths = {"listing", "listing.vehicle"})
    BookingEntity findByRenterId(Integer renterId);
    @EntityGraph(attributePaths = {"listing", "listing.vehicle"})
    List<BookingEntity> findAllByRenterId(Integer renterId);
    @EntityGraph(attributePaths = {"listing", "listing.vehicle"})
    List<BookingEntity> findAllByListingId(Integer listingId);
    Boolean existsByListingIdAndStartAtLessThanAndEndAtGreaterThan(Integer listingId, Instant endAt, Instant startAt);
}
