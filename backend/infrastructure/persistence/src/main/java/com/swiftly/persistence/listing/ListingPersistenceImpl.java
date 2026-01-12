package com.swiftly.persistence.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.helpers.Helper;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ListingPersistenceImpl implements ListingRepository {
    private final JpaListingRepository repository;
    private final JpaVehicleRepository vehicleRepository;
    private final Helper helper;

    public Listing save(Listing listing) {
        VehicleEntity vehicleEntity = vehicleRepository.findById(listing.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        ListingEntity listingEntity;

        if(listing.getBasePricePerDay() != null)
        {
            listingEntity = repository.findById(listing.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Listing not found for id " + listing.getId()));

            listingEntity.setTitle(listing.getTitle());
            listingEntity.setDescription(listing.getDescription());
            listingEntity.setBasePricePerDay(listing.getBasePricePerDay());
            listingEntity.setInstantBook(listing.getInstantBook());
        }
        else {
            listingEntity = new ListingEntity(vehicleEntity, listing.getTitle(), listing.getDescription(), listing.getBasePricePerDay(), listing.getInstantBook());
        }

        ListingEntity saved = repository.save(listingEntity);

        return helper.mapToListing(saved);
    }

    public List<Listing> findAll() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAll();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(helper.mapToListing(listingEntity));
        }

       return listingsToReturn;
    }

    public List<Listing> findAllWithVehicle() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAllWithVehicle();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(helper.mapToListing(listingEntity));
        }

        return listingsToReturn;
    }

    public Listing findById(Integer id) {
        Optional<ListingEntity> existingListing = repository.findById(id);

        ListingEntity listingEntity = existingListing.get();

        return helper.mapToListing(listingEntity);
    }

    public Listing findByVehicleId(Integer vehicleId)
    {
        ListingEntity existingListing = repository.findByVehicleId(vehicleId);

        return helper.mapToListing(existingListing);
    }

    public Boolean existsByVehicleId(Integer vehicleId)
    {
        return repository.existsByVehicleId(vehicleId);
    }
}
