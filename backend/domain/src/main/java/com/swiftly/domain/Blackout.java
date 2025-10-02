package com.swiftly.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "blackouts",
        indexes = @Index(name = "idx_blackouts_listing_start", columnList = "listing_id,start_at")
)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Blackout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Column(nullable = false, updatable = false)
    @org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
    private Instant creationDate;

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
    }
}