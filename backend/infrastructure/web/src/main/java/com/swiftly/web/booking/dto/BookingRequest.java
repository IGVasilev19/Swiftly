package com.swiftly.web.booking.dto;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.enums.booking.Status;

import java.time.Instant;

public record BookingRequest(Listing listing, Profile renter, Instant startAt, Instant endAt, Status status) { }
