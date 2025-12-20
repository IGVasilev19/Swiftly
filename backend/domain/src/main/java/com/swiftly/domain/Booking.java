package com.swiftly.domain;

import com.swiftly.domain.enums.booking.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Booking {

    private Integer id;

    private Instant startAt;

    private Instant endAt;

    private Instant creationDate;

    private Status status;

    private BigDecimal totalPrice;

    private Listing listing;

    private Profile renter;

    public Booking(Integer id)
    {
        this.id = id;
    }

    public Booking(Listing listing, Profile renter, Instant startAt, Instant endAt, Status status)
    {
        this.listing = listing;
        this.renter = renter;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    public Booking(Listing listing, Profile renter, Instant startAt, Instant endAt, Status status,  BigDecimal totalPrice)
    {
        this.listing = listing;
        this.renter = renter;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
