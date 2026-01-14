package com.swiftly.web.listing.dto;

import com.swiftly.web.vehicle.dto.VehicleResponse;

import java.math.BigDecimal;
import java.time.Instant;

public record ListingResponse(Integer id , VehicleResponse vehicle, String title, String description, Instant creationDate , BigDecimal basePricePerDay, Boolean instantBook, Boolean isRemoved){}
