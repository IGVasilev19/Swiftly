package com.swiftly.application.vehicle;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
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

    public List<Vehicle> getAllByOwner(Profile owner)
    {
        return repository.findAllByOwner(owner);
    }

    public void removeById(Integer id)
    {
        Vehicle vehicle = repository.findById(id);

        vehicle.setIsRemoved(true);

        updateVehicle(vehicle);
    }

    public void deleteById(Integer id)
    {
            repository.deleteById(id);
    }

    public Vehicle getByVin(String vin)
    {
       return repository.findByVin(vin);
    }

    public void updateVehicle(Vehicle vehicle)
    {
        repository.save(vehicle);
    }
}
