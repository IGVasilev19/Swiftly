package com.swiftly.web.vehicle.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicle")
@PreAuthorize("isAuthenticated()")
@Tag(name="vehicles")
public class VehicleController {

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle()
    {
        return new ResponseEntity<>("Vehicle added successfully", HttpStatus.OK);
    }
}
