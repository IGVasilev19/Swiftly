package com.swiftly.persistence.entities;

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
public class ListingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false, unique = true)
    private VehicleEntity vehicle;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant creationDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerDay;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean instantBook = false;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isRemoved = false;

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
    }

    public ListingEntity(Integer id)
    {
        this.id = id;
    }

    public ListingEntity(VehicleEntity vehicle, String title, String description, BigDecimal basePricePerDay, Boolean instantBook)
    {
        this.vehicle = vehicle;
        this.title = title;
        this.description = description;
        this.basePricePerDay = basePricePerDay;
        this.instantBook = instantBook;
    }
}
