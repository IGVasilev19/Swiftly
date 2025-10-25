package com.swiftly.persistence.entities;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "listings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ListingEntity extends Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Vehicle vehicle;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant creationDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerDay;

    @Column(nullable = false, length = 255)
    private String pickUpAddress;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean instantBook = false;

    @Column
    private Instant startAvailability;

    @Column
    private Instant endAvailability;

    @Column(length = 64)
    private String timeZone;

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
    }
}
