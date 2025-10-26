package com.swiftly.persistence.entities;


import com.swiftly.domain.Blackout;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(
        name = "blackouts",
        indexes = @Index(name = "idx_blackouts_listing_start", columnList = "listing_id,start_at")
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlackoutEntity extends Blackout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant creationDate;

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
    }

}
