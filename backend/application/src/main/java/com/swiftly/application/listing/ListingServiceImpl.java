package com.swiftly.application.listing;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
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
        return repository.findByVehicleId(id);
    }

    public Boolean checkExistsByVehicleId(Integer id)
    {
        return repository.existsByVehicleId(id);
    }

    @Transactional
    public void updateListing(Integer id, Listing listing)
    {
        Listing existingListing = getById(id);

        existingListing.setTitle(listing.getTitle());
        existingListing.setDescription(listing.getDescription());
        existingListing.setBasePricePerDay(listing.getBasePricePerDay());
        existingListing.setInstantBook(listing.getInstantBook());

        repository.save(existingListing);
    }

    public void deleteById(Integer id) {}
}
