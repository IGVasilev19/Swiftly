package com.swiftly.web.booking.controller;

import com.swiftly.domain.Booking;
import com.swiftly.web.booking.dto.BookingRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/booking")
@Tag(name="Booking")
public class BookingController {

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request)
    {
        return ResponseEntity.ok().build();
    }
}
