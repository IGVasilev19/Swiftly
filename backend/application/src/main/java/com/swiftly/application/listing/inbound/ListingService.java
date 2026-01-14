package com.swiftly.application.listing.inbound;

import com.swiftly.domain.Listing;

import java.util.List;

public interface ListingService {
    Listing create(Listing listing);
    List<Listing> getAll();
    Listing getById(Integer id);
    Listing getByVehicleId(Integer id);
    Boolean checkExistsByVehicleId(Integer id);
    void updateListing(Listing listing);
    void deleteById(Integer id);
    void removeById(Integer id);
    void reactivateById(Integer id);
}
