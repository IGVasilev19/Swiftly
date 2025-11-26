package com.swiftly.application.vehicle.port.outbound;

import com.swiftly.domain.Vehicle;

import java.util.List;

public interface VehicleRepository {
    Vehicle findById(Integer id);
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAll();
    List<Vehicle> findAllByOwnerId(Integer ownerId);
    List<Vehicle> findAllByRenterId(Integer renterId);
    void deleteById(Integer id);
    Vehicle findByVin(String vin);
    void update(Vehicle vehicle);
}
