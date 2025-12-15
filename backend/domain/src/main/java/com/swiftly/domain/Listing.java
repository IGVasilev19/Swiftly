package com.swiftly.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Listing {

    private Integer id;

    private Vehicle vehicle;

    private String title;

    private String description;

    private Instant creationDate;

    private BigDecimal basePricePerDay;

    private Boolean instantBook = false;

    private Instant startAvailability;

    private Instant endAvailability;

    public Listing(Vehicle vehicle, String title, String description, BigDecimal basePricePerDay, Boolean instantBook, Instant startAvailability, Instant endAvailability)
    {
        this.vehicle = vehicle;
        this.title = title;
        this.description = description;
        this.basePricePerDay = basePricePerDay;
        this.instantBook = instantBook;
        this.startAvailability = startAvailability;
        this.endAvailability = endAvailability;
    }
}
