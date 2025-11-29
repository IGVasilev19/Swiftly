package com.swiftly.web.vehicle.controller;


import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.web.vehicle.dto.VehicleCreateRequest;
import com.swiftly.web.vehicle.dto.VehicleCreateResponse;
import com.swiftly.web.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vehicle")
@PreAuthorize("isAuthenticated()")
@Tag(name="vehicles")
public class VehicleController {
    private final VehicleManagementService service;


    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(VehicleCreateRequest newVehicle)
    {
        try
        {
            VehicleCreateResponse response = VehicleMapper.toVehicleCreateResponse(service.addVehicle(VehicleMapper.toVehicle(newVehicle)));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
