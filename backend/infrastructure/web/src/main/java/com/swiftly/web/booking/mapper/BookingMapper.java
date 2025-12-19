package com.swiftly.web.booking.mapper;

import com.swiftly.domain.Booking;
import com.swiftly.web.booking.dto.BookingRequest;
import com.swiftly.web.booking.dto.BookingResponse;

public class BookingMapper {
    public static Booking toBooking(BookingRequest request)
    {
        return new Booking(request.listing(), request.renter(), request.startAt(), request.endAt(), request.status());
    }

    public static BookingResponse toBookingResponse(Booking booking)
    {
        return new BookingResponse(booking.getId(), booking.getListing(),booking.getRenter(),booking.getStartAt(),booking.getEndAt(),booking.getStatus(),booking.getCreationDate(),booking.getTotalPrice());
    }
}
