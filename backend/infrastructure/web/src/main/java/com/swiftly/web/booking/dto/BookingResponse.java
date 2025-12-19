package com.swiftly.web.booking.dto;

import com.swiftly.domain.User;
import com.swiftly.domain.enums.booking.Status;
import com.swiftly.web.listing.dto.ListingResponse;

import java.math.BigDecimal;
import java.time.Instant;
//Make a Profile DTO and Mapper, to use it in the booking and in the Vehicle Response, so you can return the name of the User
public record BookingResponse(Integer id, ListingResponse listing, User renter, Instant startAt, Instant endAt, Status status, Instant creationDate, BigDecimal totalPrice) { }
