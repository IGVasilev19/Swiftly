package com.swiftly.persistence.vehicle;

import com.swiftly.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, Integer> {
    @EntityGraph(attributePaths = {"owner", "features"})
    List<VehicleEntity> findAllByOwnerId(Integer ownerId);
    void deleteById(Integer id);
    @EntityGraph(attributePaths = {"owner", "features"})
    VehicleEntity findByVin(String vin);
    @EntityGraph(attributePaths = {"owner", "features"})
    Optional<VehicleEntity> findById(Integer id);
    Boolean existsByVin(String vin);
}