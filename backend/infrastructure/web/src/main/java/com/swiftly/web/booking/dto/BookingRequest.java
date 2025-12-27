package com.swiftly.web.booking.dto;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.enums.booking.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingRequest(@NotNull Listing listing, @NotNull LocalDate startAt, @NotNull LocalDate endAt, @NotNull BigDecimal totalPrice) { }
