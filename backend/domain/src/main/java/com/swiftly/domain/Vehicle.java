package com.swiftly.domain;

import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class Vehicle {

    private Integer id;

    private User owner;

    private String VIN;

    private String make;

    private String model;

    private String color;

    private Integer year;

    private VehicleType type;

    private FuelType fuelType;

    private Double fuelConsumption;

    private List<Feature> features;

    private String country;

    private String city;

    private Double latitude;

    private Double longitude;

    private Instant locationTimeStamp;

    public Vehicle (User owner, String VIN, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption,List<Feature> features, String country, String city)
    {
        this.owner = owner;
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.features = features;
        this.country = country;
        this.city = city;
    }

    public Vehicle (Integer id,User owner, String VIN, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption,List<Feature> features, String country, String city)
    {
        this.id = id;
        this.owner = owner;
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.features = features;
        this.country = country;
        this.city = city;
    }
}
