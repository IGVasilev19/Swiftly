package com.swiftly.domain;

import com.swiftly.domain.enums.booking.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Booking {

    private Integer id;

    private LocalDate startAt;

    private LocalDate  endAt;

    private Instant creationDate;

    private Status status;

    private BigDecimal totalPrice;

    private Listing listing;

    private Profile renter;

    public Booking(Integer id)
    {
        this.id = id;
    }

    public Booking(Listing listing, Profile renter, LocalDate  startAt, LocalDate  endAt, Status status)
    {
        this.listing = listing;
        this.renter = renter;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    public Booking(Listing listing, Profile renter, LocalDate  startAt, LocalDate  endAt, Status status,  BigDecimal totalPrice)
    {
        this.listing = listing;
        this.renter = renter;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Booking (Listing listing, LocalDate  startAt, LocalDate  endAt, BigDecimal totalPrice)
    {
        this.listing = listing;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalPrice = totalPrice;
    }
}
