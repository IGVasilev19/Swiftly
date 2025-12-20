package com.swiftly.web.booking.mapper;

import com.swiftly.domain.Booking;
import com.swiftly.web.booking.dto.BookingRequest;
import com.swiftly.web.booking.dto.BookingResponse;
import com.swiftly.web.listing.mapper.ListingMapper;
import com.swiftly.web.profile.mapper.ProfileMapper;

public class BookingMapper {
    public static Booking toBooking(BookingRequest request)
    {
        return new Booking(request.listing(), request.renter(), request.startAt(), request.endAt(), request.status());
    }

    public static BookingResponse toBookingResponse(Booking booking)
    {
        return new BookingResponse(booking.getId(), ListingMapper.toResponse(booking.getListing()), ProfileMapper.toResponse(booking.getRenter()),booking.getStartAt(),booking.getEndAt(),booking.getStatus(),booking.getCreationDate(),booking.getTotalPrice());
    }
}
