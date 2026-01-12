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

    public Listing(Integer id)
    {
        this.id = id;
    }

    public Listing(Vehicle vehicle, String title, String description, BigDecimal basePricePerDay, Boolean instantBook)
    {
        this.vehicle = vehicle;
        this.title = title;
        this.description = description;
        this.basePricePerDay = basePricePerDay;
        this.instantBook = instantBook;
    }

    public Listing(String  title, String description, BigDecimal basePricePerDay, Boolean instantBook)
    {
        this.title = title;
        this.description = description;
        this.basePricePerDay = basePricePerDay;
        this.instantBook = instantBook;
    }
}
