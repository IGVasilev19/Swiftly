package com.swiftly.web.vehicle.dto;

import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;

import java.util.List;

public record VehicleCreateResponse (Integer id, User owner, String vin, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double FuelConsumption, List<Feature> features, String country, String city, List<VehicleImage> images) { }
