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
        if(getByVehicleId(listing.getVehicle().getId()) != null)
        {
            throw new IllegalArgumentException("Listing already exists for this vehicle");
        }

        return repository.save(listing);
    }

    public List<Listing> getAll() {
        return repository.findAll();
    }

    public Listing getById(Integer id) {
        return repository.findById(id);
    }

    public Listing getByVehicleId(Integer id)
    {
        return repository.findByVehicleId(id);
    }

    public Boolean checkExistsByVehicleId(Integer id)
    {
        return repository.existsByVehicleId(id);
    }
}
