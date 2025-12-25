package com.swiftly.persistence.helpers;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;

import java.util.List;

public interface Helper {
    Vehicle mapToVehicle(VehicleEntity vehicle);
    Profile mapToProfile(ProfileEntity entity);
    List<VehicleImage> mapToVehicleImages(List<VehicleImageEntity> vehicleImages);
    Listing mapToListing(ListingEntity entity);
}
