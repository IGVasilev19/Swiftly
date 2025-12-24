package com.swiftly.persistence.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ListingPersistenceImpl implements ListingRepository {
    private final JpaListingRepository repository;
    private final com.swiftly.persistence.vehicle.JpaVehicleRepository vehicleRepository;

    public Listing save(Listing listing) {
        VehicleEntity vehicleEntity = vehicleRepository.findById(listing.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        ListingEntity listingEntity = new ListingEntity(vehicleEntity, listing.getTitle(), listing.getDescription(), listing.getBasePricePerDay(), listing.getInstantBook());
        
        if (listing.getId() != null) {
            listingEntity.setId(listing.getId());
        }

        return mapToListing(repository.save(listingEntity));
    }


    public List<Listing> findAll() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAll();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(mapToListing(listingEntity));
        }

       return listingsToReturn;
    }


    public Listing findById(Integer id) {
        Optional<ListingEntity> existingListing = repository.findById(id);
        if (existingListing.isEmpty()) return null;

        return mapToListing(existingListing.get());
    }

    public Listing findByVehicleId(Integer vehicleId)
    {
        ListingEntity existingListing = repository.findByVehicleId(vehicleId);
        if (existingListing == null) return null;

        return mapToListing(existingListing);
    }

    public Boolean existsByVehicleId(Integer vehicleId)
    {
        return repository.existsByVehicleId(vehicleId);
    }
    
    private Listing mapToListing(ListingEntity entity) {
        if (entity == null) return null;
        Vehicle vehicle = mapToVehicle(entity.getVehicle());
        return new Listing(entity.getId(), vehicle, entity.getTitle(), entity.getDescription(), entity.getCreationDate(), entity.getBasePricePerDay(), entity.getInstantBook());
    }

    private Vehicle mapToVehicle(VehicleEntity entity) {
        if (entity == null) return null;
        com.swiftly.domain.Profile owner = new com.swiftly.domain.Profile(entity.getOwner().getId(), entity.getOwner().getFullName(), entity.getOwner().getPhone(), entity.getOwner().getAvatarUrl());
        
        List<com.swiftly.domain.VehicleImage> images = new ArrayList<>();
        if (entity.getImages() != null) {
            for (com.swiftly.persistence.entities.VehicleImageEntity imgEntity : entity.getImages()) {
                 com.swiftly.domain.Vehicle shallowVehicle = new com.swiftly.domain.Vehicle(entity.getId());
                 
                 images.add(new com.swiftly.domain.VehicleImage(imgEntity.getId(), shallowVehicle, imgEntity.getData(), imgEntity.getMimeType(), imgEntity.getFileName(), imgEntity.getUploadedAt()));
            }
        }

        return new com.swiftly.domain.Vehicle(entity.getId(), owner, entity.getVin(), entity.getMake(), entity.getModel(), entity.getColor(), entity.getYear(), entity.getType(), entity.getFuelType(), entity.getFuelConsumption(), entity.getFeatures(), entity.getCountry(), entity.getCity(), images);
    }
}
