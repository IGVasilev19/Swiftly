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

    private User renter;

}
