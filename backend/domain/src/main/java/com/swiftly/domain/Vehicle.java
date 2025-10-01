package com.swiftly.domain;

import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer ownerId;
    @Column
    private Integer renterId;
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
    @Column(nullable = false)
    private List<Feature> features;
}
