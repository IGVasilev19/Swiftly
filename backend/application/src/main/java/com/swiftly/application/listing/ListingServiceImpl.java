package com.swiftly.application.listing;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListingServiceImpl implements ListingService {
    private final ListingRepository repository;

    @Transactional
    public Listing create(Listing listing) {
        if(checkExistsByVehicleId(listing.getVehicle().getId()))
        {
            throw new IllegalArgumentException("Listing already exists for this vehicle");
        }

        return repository.save(listing);
    }

    @Transactional
    public List<Listing> getAll() {
        return repository.findAllWithVehicle();
    }

    @Transactional
    public Listing getById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Listing getByVehicleId(Integer id)
    {
        Listing listing = repository.findByVehicleId(id);

        if (listing == null) {
            throw new IllegalArgumentException("Listing not found for vehicle with id " + id);
        }

        return listing;
    }

    public Boolean checkExistsByVehicleId(Integer id)
    {
        return repository.existsByVehicleId(id);
    }

    @Transactional
    public void updateListing(Listing listing)
    {
        repository.save(listing);
    }

    @Transactional
    public void removeById(Integer id)
    {
        Listing listing = repository.findById(id);

        listing.setIsRemoved(true);

        updateListing(listing);
    }

    public void deleteById(Integer id)
    {
        repository.deleteById(id);
    }

    @Transactional
    public void reactivateById(Integer id)
    {
        Listing listing = repository.findById(id);
        
        if (listing == null) {
            throw new IllegalArgumentException("Listing not found for id " + id);
        }
        
        if (!listing.getIsRemoved()) {
            throw new IllegalArgumentException("Listing is already active");
        }
        
        listing.setIsRemoved(false);
        updateListing(listing);
    }
}
