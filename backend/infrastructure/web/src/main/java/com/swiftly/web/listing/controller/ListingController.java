package com.swiftly.web.listing.controller;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.domain.Listing;
import com.swiftly.web.listing.dto.ListingRequest;
import com.swiftly.web.listing.dto.ListingResponse;
import com.swiftly.web.listing.dto.ListingUpdateRequest;
import com.swiftly.web.listing.mapper.ListingMapper;
import com.swiftly.web.vehicle.dto.VehicleUpdateRequest;
import com.swiftly.web.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/listing")
@PreAuthorize("isAuthenticated()")
@Tag(name="Listing")
public class ListingController {
    private final ListingService service;

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<?> addListing(@RequestBody ListingRequest request)
    {
        try
        {
            ListingResponse response = ListingMapper.toResponse(service.create(ListingMapper.toListing(request)));

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Listing created successfully",
                            "data", response
                    ));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('RENTER')")
    @GetMapping
    public ResponseEntity<?> getAllListings()
    {
        try
        {
            List<ListingResponse> listingsResponse = new ArrayList<>();

            List<Listing> listings = service.getAll();

            for (Listing listing : listings)
            {
                listingsResponse.add(ListingMapper.toResponse(listing));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listingsResponse);
        }catch (Exception e)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                        "message", e.getMessage()));
            }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'RENTER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getListing(@PathVariable("id") Integer id)
    {
        try
        {
            ListingResponse response = ListingMapper.toResponse(service.getById(id));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateListing(@PathVariable("id") Integer id, @RequestBody ListingUpdateRequest request)
    {
        try
        {
            Listing listing = ListingMapper.toUpdateListing(request);
            listing.setId(id);

            service.updateListing(listing);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", true,
                    "message", "Listing updated successfully"));

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }
}
