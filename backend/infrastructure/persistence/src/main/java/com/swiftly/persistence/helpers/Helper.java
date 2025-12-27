package com.swiftly.persistence.helpers;

import com.swiftly.domain.*;
import com.swiftly.persistence.entities.*;

import java.util.List;

public interface Helper {
    Vehicle mapToVehicle(VehicleEntity vehicle);
    Profile mapToProfile(ProfileEntity entity);
    List<VehicleImage> mapToVehicleImages(List<VehicleImageEntity> vehicleImages);
    Listing mapToListing(ListingEntity entity);
    Booking mapToBooking(BookingEntity entity);
}
