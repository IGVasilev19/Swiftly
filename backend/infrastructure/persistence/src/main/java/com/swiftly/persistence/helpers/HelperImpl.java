package com.swiftly.persistence.helpers;

import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelperImpl implements Helper {
    public Vehicle mapToVehicle(VehicleEntity vehicle) {
        if (vehicle == null) {
            return null;
        }
        Profile profile = vehicle.getOwner() != null ? mapToProfile(vehicle.getOwner()) : null;
        List<VehicleImage> images = vehicle.getImages() != null ? mapToVehicleImages(vehicle.getImages()) : new ArrayList<>();

        List<Feature> features = vehicle.getFeatures() != null ? new ArrayList<>(vehicle.getFeatures()) : new ArrayList<>();
        return new Vehicle(vehicle.getId(), profile, vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), features, vehicle.getCountry(), vehicle.getCity(), images);
    }

    public Profile mapToProfile(ProfileEntity entity) {
        return new Profile(entity.getId(), entity.getFullName(), entity.getPhone(), entity.getAvatarUrl());
    }

    public List<VehicleImage> mapToVehicleImages(List<VehicleImageEntity> vehicleImages) {
        List<VehicleImage> images = new ArrayList<>();
        if (vehicleImages == null) {
            return images;
        }

        for (VehicleImageEntity vehicleImage : vehicleImages) {
            if (vehicleImage != null) {
                Vehicle vehicle = vehicleImage.getVehicle() != null ? new Vehicle(vehicleImage.getVehicle().getId()) : null;
                images.add(new VehicleImage(vehicleImage.getId(), vehicle, vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
            }
        }

        return images;
    }

    public Listing mapToListing(ListingEntity entity)
    {
        if (entity == null) {
            return null;
        }

        Vehicle vehicle = entity.getVehicle() != null ? mapToVehicle(entity.getVehicle()) : null;

        return new Listing(entity.getId(), vehicle, entity.getTitle(), entity.getDescription(), entity.getCreationDate(), entity.getBasePricePerDay(), entity.getInstantBook());
    }
}
