package com.swiftly.web.vehicle.mapper;

import com.swiftly.domain.Vehicle;
import com.swiftly.web.vehicle.dto.VehicleCreateRequest;
import com.swiftly.web.vehicle.dto.VehicleCreateResponse;

public class VehicleMapper {

    public static Vehicle toVehicle(VehicleCreateRequest vehicleCreateRequest)
    {
        return new Vehicle(vehicleCreateRequest.owner(), vehicleCreateRequest.vin(), vehicleCreateRequest.make(), vehicleCreateRequest.model(), vehicleCreateRequest.color(), vehicleCreateRequest.year(), vehicleCreateRequest.type(), vehicleCreateRequest.fuelType(), vehicleCreateRequest.fuelConsumption(), vehicleCreateRequest.features(), vehicleCreateRequest.country(), vehicleCreateRequest.city(), vehicleCreateRequest.images());
    }

    public static VehicleCreateResponse toVehicleCreateResponse(Vehicle vehicle)
    {
        return new VehicleCreateResponse(vehicle.getId(), vehicle.getOwner(), vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), vehicle.getFeatures(), vehicle.getCountry(), vehicle.getCity(), vehicle.getImages());
    }
}
