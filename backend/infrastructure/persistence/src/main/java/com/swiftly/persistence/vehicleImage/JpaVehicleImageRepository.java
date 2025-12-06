package com.swiftly.persistence.vehicleImage;

import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaVehicleImageRepository extends JpaRepository<VehicleImageEntity, Integer> {
    List<VehicleImageEntity> findAllByVehicleId(Integer vehicleId);
}
