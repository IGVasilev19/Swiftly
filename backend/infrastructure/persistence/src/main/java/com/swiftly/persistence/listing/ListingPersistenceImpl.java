package com.swiftly.persistence.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.helpers.Helper;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import jakarta.transaction.Transactional;
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

        ListingEntity listingEntity = new ListingEntity(vehicleEntity, listing.getTitle(), listing.getDescription(), listing.getBasePricePerDay(), listing.getInstantBook());

        return helper.mapToListing(repository.save(listingEntity));
    }

    @Transactional
    public List<Listing> findAll() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAll();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(helper.mapToListing(listingEntity));
        }

       return listingsToReturn;
    }

    @Transactional
    public List<Listing> findAllWithVehicle() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAllWithVehicle();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(helper.mapToListing(listingEntity));
        }

        return listingsToReturn;
    }

    @Transactional
    public Listing findById(Integer id) {
        Optional<ListingEntity> existingListing = repository.findById(id);

        ListingEntity listingEntity = existingListing.get();

        return helper.mapToListing(listingEntity);
    }

    @Transactional
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
