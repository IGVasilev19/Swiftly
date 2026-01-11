package com.swiftly.web.vehicle.dto;

import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record VehicleUpdateRequest (@Pattern(regexp="^[A-HJ-NPR-Z0-9]{17}$") String vin, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption, List<Feature> features, String country, String city, Boolean listed){}
