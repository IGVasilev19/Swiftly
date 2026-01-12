package com.swiftly.web.listing.dto;

import java.math.BigDecimal;

public record ListingUpdateRequest(String title, String description, BigDecimal basePricePerDay, Boolean instantBook){}
