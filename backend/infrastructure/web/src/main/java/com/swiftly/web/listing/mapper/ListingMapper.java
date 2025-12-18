package com.swiftly.web.listing.mapper;

import com.swiftly.domain.Listing;
import com.swiftly.web.listing.dto.ListingRequest;
import com.swiftly.web.listing.dto.ListingResponse;
import com.swiftly.web.vehicle.mapper.VehicleMapper;

public class ListingMapper {
    public static Listing toListing(ListingRequest request)
    {
        return new Listing(request.vehicle(), request.title(), request.description(), request.basePricePerDay(), request.instantBook());
    }

    public static ListingResponse toResponse(Listing listing)
    {
        return new ListingResponse(listing.getId(), VehicleMapper.toVehicleResponse(listing.getVehicle()), listing.getTitle(), listing.getDescription(), listing.getCreationDate(), listing.getBasePricePerDay(), listing.getInstantBook());
    }
}
