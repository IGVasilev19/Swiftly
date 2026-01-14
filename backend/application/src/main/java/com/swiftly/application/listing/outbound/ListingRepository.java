package com.swiftly.application.listing.outbound;

import com.swiftly.domain.Listing;

import java.util.List;

public interface ListingRepository {
    Listing save(Listing listing);
    List<Listing> findAll();
    Listing findById(Integer id);
    Listing findByVehicleId(Integer id);
    Boolean existsByVehicleId(Integer id);
    List<Listing> findAllWithVehicle();
    void deleteById(Integer id);
}
