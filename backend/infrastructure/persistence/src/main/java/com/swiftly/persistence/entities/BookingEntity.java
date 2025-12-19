package com.swiftly.persistence.entities;
import com.swiftly.domain.Booking;
import com.swiftly.domain.enums.booking.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", indexes = @Index(name = "idx_bookings_listing_start", columnList = "listing_id,start_at"))
public class BookingEntity extends Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    private ListingEntity listing;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "renter_id", nullable = false)
    private UserEntity renter;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
    }

    public BookingEntity(Integer id)
    {
        this.id = id;
    }

    public BookingEntity(ListingEntity listing, UserEntity renter, Instant startAt, Instant endAt, Status status, BigDecimal totalPrice)
    {
        this.listing = listing;
        this.renter = renter;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
