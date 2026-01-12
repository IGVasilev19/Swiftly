package com.swiftly.application.listing.inbound;

import com.swiftly.domain.Listing;

import java.util.List;

public interface ListingService {
    Listing create(Listing listing);
    List<Listing> getAll();
    Listing getById(Integer id);
    Listing getByVehicleId(Integer id);
    Boolean checkExistsByVehicleId(Integer id);
    void updateListing(Integer id,Listing listing);
    void deleteById(Integer id);
}
