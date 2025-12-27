package com.swiftly.persistence.booking;

import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.domain.Booking;
import com.swiftly.persistence.entities.BookingEntity;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.helpers.Helper;
import com.swiftly.persistence.listing.JpaListingRepository;
import com.swiftly.persistence.profile.JpaProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookingPersistenceImpl implements BookingRepository {
    private final JpaBookingRepository repository;
    private final JpaListingRepository listingRepository;
    private final JpaProfileRepository profileRepository;
    private final Helper helper;

    public Booking save(Booking booking) {
        ListingEntity listingEntity = listingRepository.findById(booking.getListing().getId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        ProfileEntity renter = profileRepository.findById(booking.getRenter().getId())
                .orElseThrow(() -> new IllegalArgumentException("Renter not found"));

        BookingEntity bookingEntity = new BookingEntity(listingEntity, renter, booking.getStartAt(), booking.getEndAt(), booking.getStatus(), booking.getTotalPrice());

        return helper.mapToBooking(repository.save(bookingEntity));
    }

    public Booking findById(Integer id) {
        Optional<BookingEntity> bookingEntity = repository.findById(id);

        return helper.mapToBooking(bookingEntity.get());
    }

    public List<Booking> findAllByListingVehicleOwnerId(Integer ownerId)
    {
        List<BookingEntity> bookingEntities = repository.findAllByListingVehicleOwnerId(ownerId);
        List<Booking> bookings = new ArrayList<>();

        for(BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(helper.mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public List<Booking> findAll() {
        List<BookingEntity> bookingEntities = repository.findAll();
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(helper.mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public List<Booking> findAllByRenterId(Integer renterId) {
        List<BookingEntity> bookingEntities = repository.findAllByRenterId(renterId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(helper.mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public List<Booking> findAllByListingId(Integer listingId) {
        List<BookingEntity> bookingEntities = repository.findAllByListingId(listingId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(helper.mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public Boolean existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(Integer listingId, LocalDate endAt, LocalDate  startAt)
    {
        return repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(listingId, endAt, startAt);
    }
}
