package com.swiftly.application.listing;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListingServiceImpl implements ListingService {
    private final ListingRepository repository;

    public Listing create(Listing listing) {
        return repository.save(listing);
    }

    public List<Listing> getAll() {
        return repository.findAll();
    }

    public Listing getById(Integer id) {
        return repository.findById(id);
    }
}
