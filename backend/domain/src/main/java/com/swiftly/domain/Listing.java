package com.swiftly.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Listing {

    private Integer id;

    private String title;

    private String description;

    private Instant creationDate;

    private BigDecimal basePricePerDay;

    private String pickUpAddress;

    private Double longitude;

    private Double latitude;

    private Boolean instantBook = false;

    private Instant startAvailability;

    private Instant endAvailability;

    private String timeZone;

}
