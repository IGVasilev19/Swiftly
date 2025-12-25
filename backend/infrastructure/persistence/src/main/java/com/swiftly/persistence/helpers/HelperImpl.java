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
    public Vehicle mapToVehicle(VehicleEntity vehicle)
    {
        Profile profile = mapToProfile(vehicle.getOwner());

        List<VehicleImage> images = mapToVehicleImages(vehicle.getImages());

        List<Feature> features = new ArrayList<>(vehicle.getFeatures());

        return new Vehicle(vehicle.getId(), profile, vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), features, vehicle.getCountry(), vehicle.getCity(), images);
    }

    public Profile mapToProfile(ProfileEntity entity)
    {
        return new Profile(entity.getId(), entity.getFullName(), entity.getPhone(), entity.getAvatarUrl());
    }

    public List<VehicleImage> mapToVehicleImages(List<VehicleImageEntity> vehicleImages)
    {
        List<VehicleImage> images = new ArrayList<>();

        for (VehicleImageEntity vehicleImage : vehicleImages)
        {
           Vehicle vehicle = new Vehicle(vehicleImage.getVehicle().getId());

            images.add(new VehicleImage(vehicleImage.getId(), vehicle, vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
        }

        return images;
    }

    public Listing mapToListing(ListingEntity entity)
    {
        Vehicle vehicle = mapToVehicle(entity.getVehicle());

        return new Listing(entity.getId(), vehicle, entity.getTitle(), entity.getDescription(), entity.getCreationDate(), entity.getBasePricePerDay(), entity.getInstantBook());
    }
}
