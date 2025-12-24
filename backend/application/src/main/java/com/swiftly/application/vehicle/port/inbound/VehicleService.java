package com.swiftly.application.vehicle.port.inbound;

import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);
    Vehicle getById(Integer id);
    List<Vehicle> getAll();
    List<Vehicle> getAllByOwner(Profile owner);
    void deleteById(Integer id);
    Vehicle getByVin(String vin);
}
