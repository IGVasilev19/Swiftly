package com.swiftly.persistence.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.UserEntity;
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

        UserEntity owner = new UserEntity(listing.getVehicle().getOwner().getId());
        VehicleEntity vehicleEntity = new VehicleEntity(listing.getVehicle().getId(), owner, listing.getVehicle().getVin(), listing.getVehicle().getMake(),listing.getVehicle().getModel(),listing.getVehicle().getColor(),listing.getVehicle().getYear(),listing.getVehicle().getType(),listing.getVehicle().getFuelType(),listing.getVehicle().getFuelConsumption(),listing.getVehicle().getFeatures(),listing.getVehicle().getCountry(),listing.getVehicle().getCity());

        return repository.save(new ListingEntity(vehicleEntity, listing.getTitle(), listing.getDescription(), listing.getBasePricePerDay(), listing.getInstantBook()));
    }


    public List<Listing> findAll() {
        List<Listing> listingsToReturn = new ArrayList<>();
        List<ListingEntity> existingListings =  repository.findAll();

        for (ListingEntity listingEntity : existingListings)
        {
            listingsToReturn.add(new Listing(listingEntity.getId(), listingEntity.getVehicle(), listingEntity.getTitle(), listingEntity.getDescription(), listingEntity.getCreationDate(), listingEntity.getBasePricePerDay(), listingEntity.getInstantBook()));
        }

       return listingsToReturn;
    }


    public Listing findById(Integer id) {
        Optional<ListingEntity> existingListing = repository.findById(id);

        return new Listing(existingListing.get().getId(), existingListing.get().getVehicle(), existingListing.get().getTitle(), existingListing.get().getDescription(), existingListing.get().getCreationDate(), existingListing.get().getBasePricePerDay(), existingListing.get().getInstantBook());
    }

    public Listing findByVehicleId(Integer vehicleId)
    {
        ListingEntity existingListing = repository.findByVehicleId(vehicleId);

        return new Listing(existingListing.getId());
    }

    public Boolean existsByVehicleId(Integer vehicleId)
    {
        return repository.existsByVehicleId(vehicleId);
    }
}
