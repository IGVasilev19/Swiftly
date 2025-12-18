package com.swiftly.persistence.listing;

import com.swiftly.persistence.entities.ListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaListingRepository extends JpaRepository<ListingEntity, Integer> {
    ListingEntity findByVehicleId(Integer vehicleId);
    Boolean existsByVehicleId(Integer vehicleId);
}
