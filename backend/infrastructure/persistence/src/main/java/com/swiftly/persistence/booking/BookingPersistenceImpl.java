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
    private final com.swiftly.persistence.listing.JpaListingRepository listingRepository;
    private final com.swiftly.persistence.profile.JpaProfileRepository profileRepository;

    public Booking save(Booking booking) {
        ListingEntity listingEntity = listingRepository.findById(booking.getListing().getId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        ProfileEntity renter = profileRepository.findById(booking.getRenter().getId())
                .orElseThrow(() -> new IllegalArgumentException("Renter not found"));

        BookingEntity bookingEntity = new BookingEntity(listingEntity, renter, booking.getStartAt(), booking.getEndAt(), booking.getStatus(), booking.getTotalPrice());
        
        if (booking.getId() != null) {
            bookingEntity.setId(booking.getId());
        }

        return mapToBooking(repository.save(bookingEntity));
    }

    public Booking findById(Integer id) {
        Optional<BookingEntity> bookingEntity = repository.findById(id);
        if (bookingEntity.isEmpty()) return null;

        return mapToBooking(bookingEntity.get());
    }

    public Booking findByListingId(Integer listingId) {
        BookingEntity bookingEntity = repository.findByListingId(listingId);
        return mapToBooking(bookingEntity);
    }

    public Booking findByRenterId(Integer renterId) {
        BookingEntity bookingEntity = repository.findByRenterId(renterId);
        return mapToBooking(bookingEntity);
    }

    public List<Booking> findAll() {
        List<BookingEntity> bookingEntities = repository.findAll();
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public List<Booking> findAllByRenterId(Integer renterId) {
        List<BookingEntity> bookingEntities = repository.findAllByRenterId(renterId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public List<Booking> findAllByListingId(Integer listingId) {
        List<BookingEntity> bookingEntities = repository.findAllByListingId(listingId);
        List<Booking> bookings = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities)
        {
            bookings.add(mapToBooking(bookingEntity));
        }

        return bookings;
    }

    public Boolean existsByListingIdAndStartDateLessThanAndEndDateGreaterThan(Integer listingId, Instant endAt, Instant startAt)
    {
        return repository.existsByListingIdAndStartAtLessThanAndEndAtGreaterThan(listingId, endAt, startAt);
    }
    
    private Booking mapToBooking(BookingEntity entity) {
        if (entity == null) return null;
        com.swiftly.domain.Listing listing = mapToListing(entity.getListing());
        com.swiftly.domain.Profile renter = mapToProfile(entity.getRenter());
        
        return new Booking(entity.getId(), entity.getStartAt(), entity.getEndAt(), entity.getCreationDate(), entity.getStatus(), entity.getTotalPrice(), listing, renter);
    }
    
    private com.swiftly.domain.Listing mapToListing(ListingEntity entity) {
        if (entity == null) return null;
        com.swiftly.domain.Vehicle vehicle = mapToVehicle(entity.getVehicle());
        return new com.swiftly.domain.Listing(entity.getId(), vehicle, entity.getTitle(), entity.getDescription(), entity.getCreationDate(), entity.getBasePricePerDay(), entity.getInstantBook());
    }
    
    private com.swiftly.domain.Vehicle mapToVehicle(com.swiftly.persistence.entities.VehicleEntity entity) {
        if (entity == null) return null;
        com.swiftly.domain.Profile owner = mapToProfile(entity.getOwner());
        
        List<com.swiftly.domain.VehicleImage> images = new ArrayList<>();
        if (entity.getImages() != null) {
              for (com.swiftly.persistence.entities.VehicleImageEntity imgEntity : entity.getImages()) {
                   com.swiftly.domain.Vehicle shallowVehicle = new com.swiftly.domain.Vehicle(entity.getId());
                   images.add(new com.swiftly.domain.VehicleImage(imgEntity.getId(), shallowVehicle, imgEntity.getData(), imgEntity.getMimeType(), imgEntity.getFileName(), imgEntity.getUploadedAt()));
              }
        }
        
        return new com.swiftly.domain.Vehicle(entity.getId(), owner, entity.getVin(), entity.getMake(), entity.getModel(), entity.getColor(), entity.getYear(), entity.getType(), entity.getFuelType(), entity.getFuelConsumption(), entity.getFeatures(), entity.getCountry(), entity.getCity(), images);
    }
    
    private com.swiftly.domain.Profile mapToProfile(ProfileEntity entity) {
         if (entity == null) return null;
         return new com.swiftly.domain.Profile(entity.getId(), null, entity.getFullName(), entity.getPhone(), entity.getAvatarUrl());
    }
}
