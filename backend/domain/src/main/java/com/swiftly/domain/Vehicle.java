package com.swiftly.domain;

import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class Vehicle {

    private Integer id;

    private Integer ownerId;

    private Integer renterId;

    private String VIN;

    private String make;

    private String model;

    private String color;

    private Integer year;

    private VehicleType type;

    private FuelType fuelType;

    private Double fuelConsumption;

    private List<Feature> features;
}
