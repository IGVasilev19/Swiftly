package com.swiftly.web.booking.dto;

import com.swiftly.domain.enums.booking.Status;
import com.swiftly.web.listing.dto.ListingResponse;
import com.swiftly.web.profile.dto.ProfileResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingResponse(Integer id, ListingResponse listing, ProfileResponse renter, LocalDate startAt, LocalDate  endAt, Status status, Instant creationDate, BigDecimal totalPrice) { }
