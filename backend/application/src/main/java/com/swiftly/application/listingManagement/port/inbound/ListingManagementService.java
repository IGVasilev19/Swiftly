package com.swiftly.application.listingManagement.port.inbound;

import com.swiftly.domain.Listing;

import java.util.List;

public interface ListingManagementService {
    List<Listing> getFullListings();
}
