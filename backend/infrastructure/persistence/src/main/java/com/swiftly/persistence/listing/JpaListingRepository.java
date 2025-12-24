package com.swiftly.persistence.listing;

import com.swiftly.persistence.entities.ListingEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface JpaListingRepository extends JpaRepository<ListingEntity, Integer> {
    @EntityGraph(attributePaths = {"vehicle", "vehicle.owner", "vehicle.features", "vehicle.images"})
    ListingEntity findByVehicleId(Integer vehicleId);
    @EntityGraph(attributePaths = {"vehicle", "vehicle.owner", "vehicle.features", "vehicle.images"})
    Optional<ListingEntity> findById(Integer id);
    @EntityGraph(attributePaths = {"vehicle", "vehicle.owner", "vehicle.features", "vehicle.images"})
    List<ListingEntity> findAll();
    Boolean existsByVehicleId(Integer vehicleId);
}
