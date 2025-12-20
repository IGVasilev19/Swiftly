package com.swiftly.persistence.booking;

import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.domain.Booking;
import com.swiftly.persistence.entities.BookingEntity;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookingPersistenceImpl implements BookingRepository {
    private final JpaBookingRepository repository;

    public Booking save(Booking booking) {
        ListingEntity listingEntity = new ListingEntity(booking.getListing().getId());

        ProfileEntity renter = new ProfileEntity(booking.getRenter().getId());

        BookingEntity bookingEntity = new BookingEntity(listingEntity, renter, booking.getStartAt(), booking.getEndAt(), booking.getStatus(), booking.getTotalPrice());

        return repository.save(bookingEntity);
    }

    public Booking findById(Integer id) {
        Optional<BookingEntity> bookingEntity = repository.findById(id);

        return new Booking(id, bookingEntity.get().getStartAt(), bookingEntity.get().getEndAt(),bookingEntity.get().getCreationDate(),bookingEntity.get().getStatus(),bookingEntity.get().getTotalPrice(),bookingEntity.get().getListing(),bookingEntity.get().getRenter());
    }

    public Booking findByListingId(Integer listingId) {
        BookingEntity bookingEntity = repository.findByListingId(listingId);

        return new Booking(bookingEntity.getId(), bookingEntity.getStartAt(), bookingEntity.getEndAt(),bookingEntity.getCreationDate(),bookingEntity.getStatus(),bookingEntity.getTotalPrice(),bookingEntity.getListing(),bookingEntity.getRenter());
    }

    public Booking findByRenterId(Integer renterId) {
        BookingEntity bookingEntity = repository.findByRenterId(renterId);

        return new Booking(bookingEntity.getId(), bookingEntity.getStartAt(), bookingEntity.getEndAt(),bookingEntity.getCreationDate(),bookingEntity.getStatus(),bookingEntity.getTotalPrice(),bookingEntity.getListing(),bookingEntity.getRenter());
    }

    public List<Booking> findAll() {
        List<BookingEntity> bookingEntities = repository.findAll();
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(new Booking(bookingEntity.getId(), bookingEntity.getStartAt(), bookingEntity.getEndAt(),bookingEntity.getCreationDate(),bookingEntity.getStatus(),bookingEntity.getTotalPrice(),bookingEntity.getListing(),bookingEntity.getRenter()));
        }

        return bookings;
    }

    public List<Booking> findAllByRenterId(Integer renterId) {
        List<BookingEntity> bookingEntities = repository.findAllByRenterId(renterId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(new Booking(bookingEntity.getId(), bookingEntity.getStartAt(), bookingEntity.getEndAt(),bookingEntity.getCreationDate(),bookingEntity.getStatus(),bookingEntity.getTotalPrice(),bookingEntity.getListing(),bookingEntity.getRenter()));
        }

        return bookings;
    }

    public List<Booking> findAllByListingId(Integer listingId) {
        List<BookingEntity> bookingEntities = repository.findAllByListingId(listingId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(new Booking(bookingEntity.getId(), bookingEntity.getStartAt(), bookingEntity.getEndAt(),bookingEntity.getCreationDate(),bookingEntity.getStatus(),bookingEntity.getTotalPrice(),bookingEntity.getListing(),bookingEntity.getRenter()));
        }

        return bookings;
    }

    public Boolean existsByListingIdAndStartDateLessThanAndEndDateGreaterThan(Integer listingId, Instant endAt, Instant startAt)
    {
        return repository.existsByListingIdAndStartAtLessThanAndEndAtGreaterThan(listingId, endAt, startAt);
    }
}
