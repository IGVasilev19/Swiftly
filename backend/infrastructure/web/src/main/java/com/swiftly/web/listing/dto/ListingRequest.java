package com.swiftly.web.listing.dto;

import com.swiftly.domain.Vehicle;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ListingRequest(@NotNull Vehicle vehicle, @NotBlank String title, @NotBlank String description, @NotNull BigDecimal basePricePerDay, @NotNull Boolean instantBook){}
