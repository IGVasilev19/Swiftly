package com.swiftly.persistence.booking;

import com.swiftly.persistence.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, Integer> {
    @Query("""
    SELECT DISTINCT b
    FROM BookingEntity b 
    LEFT JOIN FETCH b.renter r
    LEFT JOIN FETCH b.listing l
    LEFT JOIN FETCH l.vehicle v
    LEFT JOIN FETCH v.owner 
    WHERE b.id = :id
    """)
    Optional<BookingEntity> findById(@Param("id") Integer id);

    @Query("""
    SELECT DISTINCT b
    FROM BookingEntity b 
    LEFT JOIN FETCH b.renter r
    LEFT JOIN FETCH b.listing l
    LEFT JOIN FETCH l.vehicle v
    LEFT JOIN FETCH v.owner 
    WHERE r.id = :renterId
    """)
    List<BookingEntity> findAllByRenterId(@Param("renterId")Integer renterId);

    @Query("""
    SELECT DISTINCT b
    FROM BookingEntity b
    LEFT JOIN FETCH b.listing l
    LEFT JOIN FETCH l.vehicle v
    LEFT JOIN FETCH v.owner
    WHERE l.id = :listingId
    ORDER BY b.creationDate
    """)
    List<BookingEntity> findAllByListingId(@Param("listingId") Integer listingId);

    @Query("""
    SELECT DISTINCT b
    FROM BookingEntity b 
    LEFT JOIN FETCH b.renter r
    JOIN FETCH b.listing l
    JOIN FETCH l.vehicle v
    JOIN FETCH v.owner 
    WHERE v.owner.id = :ownerId
    ORDER BY b.creationDate
    """)
    List<BookingEntity> findAllByListingVehicleOwnerId(@Param("ownerId") Integer ownerId);

    Boolean existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(Integer listingId, LocalDate endAt, LocalDate  startAt);
}
