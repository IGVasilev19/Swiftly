package com.swiftly.application.listingManagement;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.listingManagement.port.inbound.ListingManagementService;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.domain.Listing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListingManagementServiceImpl implements ListingManagementService {
    private final ListingService listingService;
    private final VehicleManagementService vehicleService;

    public List<Listing> getFullListings()
    {
        List<Listing> listings = listingService.getAll();

        for(Listing listing : listings)
        {
            listing.setVehicle( vehicleService.getFullVehicleById(listing.getVehicle().getId()));
        }

        return listings;
    }

    public Listing getFullListing(Integer id)
    {
        Listing listing = listingService.getById(id);

        listing.setVehicle( vehicleService.getFullVehicleById(listing.getVehicle().getId()));

        return listing;
    }
}
