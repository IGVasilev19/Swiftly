package com.swiftly.web.vehicle.dto;

import com.swiftly.domain.User;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record VehicleCreateRequest(@NotBlank User owner, @NotBlank @Pattern(regexp="^[A-HJ-NPR-Z0-9]{17}$") String vin, @NotBlank String make, @NotBlank String model, @NotBlank String color, @NotBlank Integer year, @NotBlank VehicleType type, @NotBlank FuelType fuelType, @NotBlank Double fuelConsumption, List<Feature> features, @NotBlank String country, @NotBlank String city, @NotBlank List<VehicleImage> images) {
}
