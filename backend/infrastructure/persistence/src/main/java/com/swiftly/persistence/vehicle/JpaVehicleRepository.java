package com.swiftly.persistence.vehicle;

import com.swiftly.domain.Vehicle;
import com.swiftly.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, Integer> {
    List<VehicleEntity> findAllByOwnerId(Integer ownerId);
    void deleteById(Integer id);
    VehicleEntity findByVin(String vin);
}