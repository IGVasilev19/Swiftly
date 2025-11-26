package com.swiftly.application.vehicle;

import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Vehicle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository repository;

    public Vehicle getById(Integer id)
    {
        return repository.findById(id);
    }

    public Vehicle save(Vehicle vehicle)
    {
        return repository.save(vehicle);
    }
}
