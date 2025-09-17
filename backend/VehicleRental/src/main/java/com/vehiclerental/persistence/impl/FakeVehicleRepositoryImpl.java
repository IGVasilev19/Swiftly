package com.vehiclerental.persistence.impl;

import org.springframework.stereotype.Repository;
import com.vehiclerental.persistence.VehicleRepository;
import com.vehiclerental.persistence.entity.VehicleEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FakeVehicleRepositoryImpl implements VehicleRepository {
    private static Integer NEXT_ID = 1;
    private final List<VehicleEntity> savedVehicles;

    public FakeVehicleRepositoryImpl() {
        this.savedVehicles = new ArrayList<>();
    }

    public FakeVehicleRepositoryImpl(List<VehicleEntity> savedVehicles) {
        this.savedVehicles = savedVehicles;
    }

    @Override
    public boolean existsByVin(String vin) {
        return this.savedVehicles
                .stream()
                .anyMatch(vehicleEntity -> vehicleEntity.getVIN().equals(vin));
    }

    @Override
    public VehicleEntity save(VehicleEntity vehicle) {
        if (vehicle.getId() == null) {
            vehicle.setId(NEXT_ID);
            NEXT_ID++;
            this.savedVehicles.add(vehicle);
        }
        return vehicle;
    }

    @Override
    public void deleteById(Integer vehicleId) {
        this.savedVehicles.removeIf(vehicleEntity -> vehicleEntity.getId().equals(vehicleId));
    }

    @Override
    public VehicleEntity update(VehicleEntity vehicle) {
        VehicleEntity vehicleToUpdate = savedVehicles.get(vehicle.getId());
        vehicleToUpdate.setColor(vehicle.getColor());

        return vehicleToUpdate;
    }

    @Override
    public VehicleEntity findById(Integer vehicleId)
    {
        return savedVehicles.stream().filter(vehicleEntity -> vehicleEntity.getId().equals(vehicleId)).findFirst().orElse(null);
    }

    @Override
    public List<VehicleEntity> getAll()
    {
        return Collections.unmodifiableList(this.savedVehicles);
    }

}
