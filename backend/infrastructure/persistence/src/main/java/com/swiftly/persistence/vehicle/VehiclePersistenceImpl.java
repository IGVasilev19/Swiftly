package com.swiftly.persistence.vehicle;

import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import com.swiftly.persistence.helpers.Helper;
import com.swiftly.persistence.profile.JpaProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VehiclePersistenceImpl implements VehicleRepository {
    private final JpaVehicleRepository repository;
    private final JpaProfileRepository profileRepository;
    private final Helper helper;



    public Vehicle save(Vehicle vehicle)
    {
        ProfileEntity profileEntity = profileRepository.findById(vehicle.getOwner().getId()).orElseThrow(() -> new IllegalArgumentException("Profile not found for id " + vehicle.getOwner().getId()));

        VehicleEntity vehicleEntity;
        
        if (vehicle.getId() != null && vehicle.getIsRemoved() == false)
        {
            vehicleEntity = repository.findById(vehicle.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for id " + vehicle.getId()));
            
            vehicleEntity.setOwner(profileEntity);
            vehicleEntity.setVin(vehicle.getVin());
            vehicleEntity.setMake(vehicle.getMake());
            vehicleEntity.setModel(vehicle.getModel());
            vehicleEntity.setColor(vehicle.getColor());
            vehicleEntity.setYear(vehicle.getYear());
            vehicleEntity.setType(vehicle.getType());
            vehicleEntity.setFuelType(vehicle.getFuelType());
            vehicleEntity.setFuelConsumption(vehicle.getFuelConsumption());
            vehicleEntity.setFeatures(vehicle.getFeatures());
            vehicleEntity.setCountry(vehicle.getCountry());
            vehicleEntity.setCity(vehicle.getCity());

        } else if(vehicle.getId() != null) {
            vehicleEntity = repository.findById(vehicle.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for id " + vehicle.getId()));

            vehicleEntity.setIsRemoved(true);
        }else {
            vehicleEntity = new VehicleEntity(profileEntity, vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), vehicle.getFeatures(), vehicle.getCountry(), vehicle.getCity());
        }

        VehicleEntity saved = repository.save(vehicleEntity);

        return helper.mapToVehicle(saved);
    }


    @Transactional(readOnly = true)
    public Vehicle findById(Integer id)
    {
        Optional<VehicleEntity> vehicleEntityOptional = repository.findById(id);

        VehicleEntity vehicleEntity = vehicleEntityOptional.get();

        return helper.mapToVehicle(vehicleEntity);
    }


    public List<Vehicle> findAll() {
        List<VehicleEntity> vehicleList = repository.findAll();
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            vehicles.add(helper.mapToVehicle(vehicleEntity));
        }

        return vehicles;
    }



    @Transactional(readOnly = true)
    public List<Vehicle> findAllByOwner(Profile owner) {
        List<VehicleEntity> vehicleList = repository.findAllByOwner(owner.getId());
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            vehicles.add(helper.mapToVehicle(vehicleEntity));
        }

        return vehicles;
    }


    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Vehicle findByVin(String vin) {
        VehicleEntity vehicleEntity = repository.findByVin(vin);

        return helper.mapToVehicle(vehicleEntity);
    }

    public Boolean existsByVin(String vin)
    {
        return repository.existsByVin(vin);
    }

    public void addNewImage(Vehicle vehicle, VehicleImage vehicleImage)
    {
        VehicleEntity vehicleEntity = repository.findById(vehicle.getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        
        VehicleImageEntity vehicleImageEntity = new VehicleImageEntity(vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt());
        
        vehicleEntity.addImage(vehicleImageEntity);
        repository.save(vehicleEntity);
    }

    public void removeImage(VehicleImage vehicleImage)
    {
        VehicleEntity vehicleEntity = repository.findById(vehicleImage.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        vehicleEntity.removeImageById(vehicleImage.getId());
        repository.save(vehicleEntity);
    }
}
