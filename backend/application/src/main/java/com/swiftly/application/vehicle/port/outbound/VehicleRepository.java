package com.swiftly.application.vehicle.port.outbound;

import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;

import java.util.List;

public interface VehicleRepository {
    Vehicle findById(Integer id);
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAll();
    List<Vehicle> findAllByOwnerId(Integer ownerId);
    void deleteById(Integer id);
    Vehicle findByVin(String vin);
    Boolean existsByVin(String vin);
    void addNewImage(Vehicle vehicle, VehicleImage vehicleImage);
    void removeImage(VehicleImage vehicleImage);
}
