package com.swiftly.web.enums.controller;

import com.swiftly.domain.enums.booking.Status;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/enums")
public class EnumController {

    @GetMapping("/vehicle-types")
    public VehicleType[] vehicleTypes() {
        return VehicleType.values();
    }

    @GetMapping("/fuel-types")
    public FuelType[] fuelTypes() {
        return FuelType.values();
    }

    @GetMapping("/features")
    public Feature[] features() {
        return Feature.values();
    }
}
