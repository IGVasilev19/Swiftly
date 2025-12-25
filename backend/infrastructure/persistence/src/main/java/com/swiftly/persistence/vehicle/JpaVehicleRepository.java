package com.swiftly.persistence.vehicle;

import com.swiftly.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, Integer> {
    @Query("SELECT DISTINCT v FROM VehicleEntity v LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.images WHERE v.owner.id = :ownerId")
    List<VehicleEntity> findAllByOwner(@Param("ownerId") Integer ownerId);
    void deleteById(Integer id);
    @Query("SELECT DISTINCT v FROM VehicleEntity v LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.images WHERE v.vin = :vin")
    VehicleEntity findByVin(@Param("vin") String vin);
    @Query("SELECT DISTINCT v FROM VehicleEntity v LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.images WHERE v.id = :id")
    Optional<VehicleEntity> findById(@Param("id") Integer id);
    Boolean existsByVin(String vin);
}