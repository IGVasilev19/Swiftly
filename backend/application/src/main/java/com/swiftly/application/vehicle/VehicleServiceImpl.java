package com.swiftly.application.vehicle;

import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository repository;

    public Vehicle create(Vehicle vehicle)
    {
        if(repository.existsByVin(vehicle.getVin()))
        {
            throw new IllegalArgumentException("Vehicle already exists");
        }

        return repository.save(vehicle);
    }

    public Vehicle getById(Integer id)
    {
        return repository.findById(id);
    }

    public List<Vehicle> getAll()
    {
        return repository.findAll();
    }

    public List<Vehicle> getAllByOwnerId(Integer ownerId)
    {
        return repository.findAllByOwnerId(ownerId);
    }

    public void deleteById(Integer id)
    {
        repository.deleteById(id);
    }

    public Vehicle getByVin(String vin)
    {
       return repository.findByVin(vin);
    }

}
