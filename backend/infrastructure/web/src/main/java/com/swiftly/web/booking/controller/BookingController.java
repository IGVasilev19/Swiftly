package com.swiftly.web.booking.controller;

import com.swiftly.application.booking.port.inbound.BookingService;
import com.swiftly.domain.Booking;
import com.swiftly.domain.Listing;
import com.swiftly.domain.User;
import com.swiftly.web.booking.dto.BookingRequest;
import com.swiftly.web.booking.dto.BookingResponse;
import com.swiftly.web.booking.mapper.BookingMapper;
import com.swiftly.web.listing.dto.ListingResponse;
import com.swiftly.web.listing.mapper.ListingMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/booking")
@Tag(name="Booking")
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasRole('RENTER')")
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request)
    {
        try
        {
            BookingResponse response = BookingMapper.toResponse(bookingService.create(BookingMapper.toBooking(request)));

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Booking created successfully",
                            "data", response
                    ));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('RENTER')")
    @GetMapping("/me")
    public ResponseEntity<?> getAllRenterBookings()
    {
        try
        {
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<BookingResponse> bookingResponse = new ArrayList<>();

            List<Booking> bookings = bookingService.getAllByRenterId(loggedUser.getId());

            for (Booking booking : bookings)
            {
                bookingResponse.add(BookingMapper.toResponse(booking));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookingResponse);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owned")
    public ResponseEntity<?> getAllOwnerBookings()
    {
        try
        {
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<BookingResponse> bookingResponse = new ArrayList<>();

            List<Booking> bookings = bookingService.getAllByListingVehicleOwnerId(loggedUser.getId());

            for (Booking booking : bookings)
            {
                bookingResponse.add(BookingMapper.toResponse(booking));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookingResponse);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable("id") Integer id)
    {
        try
        {
            BookingResponse response = BookingMapper.toResponse(bookingService.getById(id));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<?> getBookingsByListingId(@PathVariable("listingId") Integer listingId)
    {
        try
        {
            List<Booking> bookings = bookingService.getAllByListingId(listingId);
            List<BookingResponse> response = new ArrayList<>();

            for (Booking booking : bookings)
            {
                response.add(BookingMapper.toResponse(booking));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }
}
