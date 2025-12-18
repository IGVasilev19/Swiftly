package com.swiftly.web.listing.controller;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.web.listing.dto.ListingRequest;
import com.swiftly.web.listing.dto.ListingResponse;
import com.swiftly.web.listing.mapper.ListingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/listing")
@PreAuthorize("isAuthenticated()")
public class ListingController {
    private final ListingService service;

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<?> addListing(@RequestBody ListingRequest request)
    {
        ListingResponse response = ListingMapper.toResponse(service.create(ListingMapper.toListing(request)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Listing created successfully",
                        "data", response
                ));
    }
}
