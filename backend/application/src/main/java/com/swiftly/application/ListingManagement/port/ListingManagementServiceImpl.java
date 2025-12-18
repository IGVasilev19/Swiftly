package com.swiftly.application.ListingManagement.port;

import com.swiftly.application.ListingManagement.port.inbound.ListingManagementService;
import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
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
}
