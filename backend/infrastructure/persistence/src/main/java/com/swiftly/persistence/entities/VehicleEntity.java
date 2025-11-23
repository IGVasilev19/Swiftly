package com.swiftly.persistence.entities;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
public class VehicleEntity extends Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false,  length = 17, unique = true)
    private String VIN;

    @Column(nullable = false, length = 100)
    private String make;

    @Column(nullable = false,  length = 100)
    private String model;

    @Column(nullable = false,   length = 100)
    private String color;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private VehicleType type;

    @Column(nullable = false)
    private FuelType fuelType;

    @Column(nullable = false)
    private Double fuelConsumption;

    @ElementCollection(targetClass = Feature.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "vehicle_features",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "feature", nullable = false)
    private List<Feature> features;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(length = 100)
    private Double latitude;

    @Column(length = 100)
    private Double longitude;

    @Column(length = 100)
    private Instant locationTimeStamp;

}
