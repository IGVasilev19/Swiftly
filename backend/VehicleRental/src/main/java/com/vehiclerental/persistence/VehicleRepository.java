package com.vehiclerental.persistence;

import com.vehiclerental.persistence.entity.VehicleEntity;

import java.util.List;

public interface VehicleRepository {
    boolean existsByVin(String vin);

    List<VehicleEntity> getAll();

    VehicleEntity save(VehicleEntity vehicle);

    void deleteById(Integer vehicleId);

    VehicleEntity update(VehicleEntity vehicle);

    VehicleEntity findById(Integer vehicleId);
}
