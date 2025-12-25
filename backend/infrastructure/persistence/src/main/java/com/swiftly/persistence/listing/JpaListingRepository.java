package com.swiftly.persistence.listing;

import com.swiftly.persistence.entities.ListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface JpaListingRepository extends JpaRepository<ListingEntity, Integer> {
    @Query("""
        select distinct l 
        from ListingEntity l 
        left join fetch l.vehicle v 
        left join fetch v.owner 
        left join fetch v.images
        where v.id = :vehicleId
        """)
    ListingEntity findByVehicleId(@Param("vehicleId") Integer vehicleId);
    @Query("""
        select distinct l 
        from ListingEntity l 
        left join fetch l.vehicle v 
        left join fetch v.owner 
        left join fetch v.images
        where l.id = :id
        """)
    Optional<ListingEntity> findById(@Param("id") Integer id);
    @Query("""
        select distinct l 
        from ListingEntity l 
        left join fetch l.vehicle v 
        left join fetch v.owner 
        left join fetch v.images
        order by l.creationDate
        """)
    List<ListingEntity> findAllWithVehicle();
    Boolean existsByVehicleId(Integer vehicleId);
}
