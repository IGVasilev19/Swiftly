package com.swiftly.persistence.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
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

    public Listing save(Listing listing) {

        VehicleEntity vehicleEntity = new VehicleEntity(listing.getVehicle().getId());

        return repository.save(new ListingEntity(vehicleEntity, listing.getTitle(), listing.getDescription(), listing.getBasePricePerDay(), listing.getInstantBook(), listing.getStartAvailability(), listing.getEndAvailability()));
    }


    public List<Listing> findAll() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAll();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(new Listing(listingEntity.getId(), listingEntity.getVehicle(), listingEntity.getTitle(), listingEntity.getDescription(), listingEntity.getCreationDate(), listingEntity.getBasePricePerDay(), listingEntity.getInstantBook(), listingEntity.getStartAvailability(), listingEntity.getEndAvailability()));
        }

       return listingsToReturn;
    }


    public Listing findById(Integer id) {
        Optional<ListingEntity> existingListing = repository.findById(id);

        return new Listing(existingListing.get().getId(), existingListing.get().getVehicle(), existingListing.get().getTitle(), existingListing.get().getDescription(), existingListing.get().getCreationDate(), existingListing.get().getBasePricePerDay(), existingListing.get().getInstantBook(), existingListing.get().getStartAvailability(), existingListing.get().getEndAvailability());
    }
}
