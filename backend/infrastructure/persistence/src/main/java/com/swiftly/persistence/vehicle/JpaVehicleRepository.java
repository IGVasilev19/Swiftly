package com.swiftly.persistence.vehicle;

import com.swiftly.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, Integer> {
    @Query("SELECT DISTINCT v FROM VehicleEntity v LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.features WHERE v.owner.id = :ownerId")
    List<VehicleEntity> findAllByOwner(@Param("ownerId") Integer ownerId);
    void deleteById(Integer id);
    @EntityGraph(attributePaths = {"owner", "features", "images"})
    VehicleEntity findByVin(String vin);
    @EntityGraph(attributePaths = {"owner", "features", "images"})
    Optional<VehicleEntity> findById(Integer id);
    Boolean existsByVin(String vin);
}