package com.swiftly.web.vehicle.controller;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.web.vehicle.dto.VehicleRequest;
import com.swiftly.web.vehicle.dto.VehicleResponse;
import com.swiftly.web.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vehicle")
@PreAuthorize("isAuthenticated()")
@Tag(name="Vehicle")
public class VehicleController {
    private final VehicleManagementService service;
    private final VehicleService vehicleService;
    private final ListingService listingService;
    private final ProfileService profileService;


    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addVehicle(@RequestPart("vehicleData") @Valid VehicleRequest newVehicle, @RequestPart("images") List<MultipartFile> images)
    {
        try
        {
            VehicleResponse response = VehicleMapper.toVehicleResponse(service.addVehicle(VehicleMapper.toVehicle(newVehicle), images));

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Vehicle created successfully",
                    "vehicle", response
            ));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owned")
    public ResponseEntity<?> getOwnedVehicles()
    {
        try
        {
            User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Profile ownerProfile = profileService.getById(owner.getId());

            List<VehicleResponse> ownedVehicles = new ArrayList<>();

            List<Vehicle> vehicles = vehicleService.getAllByOwner(ownerProfile);

            for(Vehicle vehicle : vehicles)
            {
                if(listingService.checkExistsByVehicleId(vehicle.getId()))
                {
                  VehicleResponse response = VehicleMapper.toVehicleResponse(vehicle);
                  VehicleResponse listedVehicle =  response.withListed(true);
                  ownedVehicles.add(listedVehicle);
                }
                else
                {
                    ownedVehicles.add(VehicleMapper.toVehicleResponse(vehicle));
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(ownedVehicles);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable("id") Integer id)
    {
        try
        {
           VehicleResponse vehicle = VehicleMapper.toVehicleResponse(vehicleService.getById(id));

            if(listingService.checkExistsByVehicleId(vehicle.id()))
            {
                VehicleResponse listedvehicle = vehicle.withListed(true);

                return ResponseEntity.status(HttpStatus.OK).body(listedvehicle);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.OK).body(vehicle);
            }

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false,
                    "message", e.getMessage()));
        }
    }
}
