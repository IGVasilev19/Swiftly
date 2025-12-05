package com.swiftly.web.vehicle.dto;

import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.validation.constraints.*;

import java.util.List;

public record VehicleCreateRequest(@NotBlank @Pattern(regexp="^[A-HJ-NPR-Z0-9]{17}$") String vin, @NotBlank String make, @NotBlank String model, @NotBlank String color, @NotNull Integer year, @NotNull VehicleType type, @NotNull FuelType fuelType, @NotNull @DecimalMin(value="0.1") Double fuelConsumption, List<Feature> features, @NotBlank String country, @NotBlank String city) {
}
