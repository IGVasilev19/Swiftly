package com.swiftly.persistence.listing;

import com.swiftly.persistence.entities.ListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface JpaListingRepository extends JpaRepository<ListingEntity, Integer> {
    @Query("""
        SELECT DISTINCT l 
        FROM ListingEntity l 
        LEFT JOIN FETCH l.vehicle v
        WHERE v.id = :vehicleId
        """)
    ListingEntity findByVehicleId(@Param("vehicleId") Integer vehicleId);

    @Query("""
        SELECT DISTINCT l 
        FROM ListingEntity l 
        LEFT JOIN FETCH l.vehicle v 
        WHERE l.id = :id
        """)
    Optional<ListingEntity> findById(@Param("id") Integer id);

    @Query("""
        SELECT DISTINCT l 
        FROM ListingEntity l 
        LEFT JOIN FETCH l.vehicle v 
        ORDER BY l.creationDate
        """)
    List<ListingEntity> findAllWithVehicle();

    Boolean existsByVehicleId(Integer vehicleId);

    void deleteById(Integer id);
}
