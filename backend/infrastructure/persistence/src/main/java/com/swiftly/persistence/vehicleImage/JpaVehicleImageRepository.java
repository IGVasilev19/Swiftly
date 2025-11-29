package com.swiftly.persistence.vehicleImage;

import com.swiftly.persistence.entities.VehicleImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVehicleImageRepository extends JpaRepository<VehicleImageEntity, Integer> {
}
