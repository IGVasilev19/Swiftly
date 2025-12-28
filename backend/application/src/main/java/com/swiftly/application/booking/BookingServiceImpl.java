package com.swiftly.application.booking;

import com.swiftly.application.booking.port.inbound.BookingService;
import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.domain.Booking;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.booking.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ProfileService profileService;

    @Transactional
    public Booking create(Booking booking)
    {
        if(bookingAlreadyExists(booking.getListing().getId(), booking.getEndAt(), booking.getStartAt()))
        {
            throw new IllegalArgumentException("Booking already exists");
        }

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile renter = profileService.getById(loggedUser.getId());
        booking.setRenter(renter);

        if(booking.getListing().getInstantBook())
        {
            booking.setStatus(Status.APPROVED);
        }
        else
        {
            booking.setStatus(Status.REQUESTED);
        }

        return repository.save(booking);
    }

    @Transactional
    public Booking getById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Booking> getAllByRenterId(Integer renterId)
    {
        return repository.findAllByRenterId(renterId);
    }

    @Transactional
    public List<Booking> getAllByListingId(Integer listingId) {
        return repository.findAllByListingId(listingId);
    }

    public Boolean bookingAlreadyExists(Integer listingId, LocalDate endAt, LocalDate  startAt) {
        return repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(listingId, endAt, startAt);
    }

    @Transactional
    public List<Booking> getAllByListingVehicleOwnerId(Integer ownerId)
    {
        return repository.findAllByListingVehicleOwnerId(ownerId);
    }

}
