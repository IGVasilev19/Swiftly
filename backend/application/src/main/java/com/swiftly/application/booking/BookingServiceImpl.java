package com.swiftly.application.booking;

import com.swiftly.application.booking.port.inbound.BookingService;
import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.domain.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    public Booking create(Booking booking)
    {
        if(bookingAlreadyExists(booking.getListing().getId(), booking.getStartAt(), booking.getEndAt()))
        {
            throw new IllegalArgumentException("Booking already exists");
        }

        calculateTotalPrice(booking);

        return repository.save(booking);
    }

    public Booking getById(Integer id) {
        return repository.findById(id);
    }

    public Booking getByListingId(Integer listingId) {
        return repository.findByListingId(listingId);
    }

    public Booking getByRenterId(Integer renterId) {
        return repository.findByRenterId(renterId);
    }

    public List<Booking> getAll() {
        return repository.findAll();
    }

    public List<Booking> getAllByRenterId(Integer renterId) {
        return repository.findAllByRenterId(renterId);
    }

    public List<Booking> getAllByListingId(Integer listingId) {
        return repository.findAllByListingId(listingId);
    }

    public Boolean bookingAlreadyExists(Integer listingId, Instant end, Instant start) {
        return repository.existsByListingIdAndStartDateLessThanAndEndDateGreaterThan(listingId, start, end);
    }

    public void calculateTotalPrice(Booking booking) {
        long days = ChronoUnit.DAYS.between(booking.getStartAt(), booking.getEndAt());

        BigDecimal totalPrice = new BigDecimal(days).multiply(new BigDecimal(days));

        booking.setTotalPrice(totalPrice);
    }
}
