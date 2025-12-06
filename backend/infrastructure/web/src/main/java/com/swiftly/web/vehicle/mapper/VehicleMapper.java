package com.swiftly.web.vehicle.mapper;

import com.swiftly.domain.Vehicle;
import com.swiftly.web.vehicle.dto.VehicleRequest;
import com.swiftly.web.vehicle.dto.VehicleResponse;

public class VehicleMapper {

    public static Vehicle toVehicle(VehicleRequest vehicleRequest)
    {
        return new Vehicle(vehicleRequest.vin(), vehicleRequest.make(), vehicleRequest.model(), vehicleRequest.color(), vehicleRequest.year(), vehicleRequest.type(), vehicleRequest.fuelType(), vehicleRequest.fuelConsumption(), vehicleRequest.features(), vehicleRequest.country(), vehicleRequest.city());
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle)
    {
        return new VehicleResponse(vehicle.getId(), vehicle.getOwner().getId(), vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), vehicle.getFeatures(), vehicle.getCountry(), vehicle.getCity(), vehicle.getImages());
    }
}
