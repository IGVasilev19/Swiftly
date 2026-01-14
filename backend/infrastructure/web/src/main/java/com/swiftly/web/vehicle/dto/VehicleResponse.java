package com.swiftly.web.vehicle.dto;

import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.web.profile.dto.ProfileResponse;

import java.util.List;

public record VehicleResponse(Integer id, ProfileResponse owner, String vin, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption, List<Feature> features, String country, String city, List<VehicleImage> images, Boolean listed, Boolean isRemoved)
{
    public VehicleResponse withListed(Boolean listed)
    {
        return new VehicleResponse(id, owner, vin, make, model, color, year, type, fuelType, fuelConsumption, features, country, city, images, listed, isRemoved);
    }
}
