package com.swiftly.persistence.vehicle;

import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VehiclePersistenceImpl implements VehicleRepository {
    private final JpaVehicleRepository repository;

    public Vehicle save(Vehicle vehicle)
    {
        ProfileEntity profileEntity = new ProfileEntity(vehicle.getOwner().getId());

        VehicleEntity vehicleEntity = new VehicleEntity(profileEntity, vehicle.getVin(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), vehicle.getFeatures(), vehicle.getCountry(), vehicle.getCity());
        
        return repository.save(vehicleEntity);
    }

    public Vehicle findById(Integer id)
    {
        Optional<VehicleEntity> vehicleEntity = repository.findById(id);
        List<VehicleImage> images = new ArrayList<>();

        for(VehicleImage vehicleImage : vehicleEntity.get().getImages())
        {
            images.add(new VehicleImage(vehicleImage.getId(), vehicleImage.getVehicle(), vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
        }

        return new Vehicle(id, vehicleEntity.get().getOwner(), vehicleEntity.get().getVin(), vehicleEntity.get().getMake(),vehicleEntity.get().getModel(), vehicleEntity.get().getColor(), vehicleEntity.get().getYear(), vehicleEntity.get().getType(), vehicleEntity.get().getFuelType(), vehicleEntity.get().getFuelConsumption(), vehicleEntity.get().getFeatures(), vehicleEntity.get().getCountry(), vehicleEntity.get().getCity(), images);
    }


    public List<Vehicle> findAll() {
        List<VehicleEntity> vehicleList = repository.findAll();
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            Profile profile = new Profile(vehicleEntity.getOwner().getId());

            List<VehicleImage> images = new ArrayList<>();

            for(VehicleImage vehicleImage : vehicleEntity.getImages())
            {
                images.add(new VehicleImage(vehicleImage.getId(), vehicleImage.getVehicle(), vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
            }

            vehicles.add(new Vehicle(vehicleEntity.getId(), profile, vehicleEntity.getVin(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity(), images));
        }

        return vehicles;
    }


    public List<Vehicle> findAllByOwnerId(Integer ownerId) {
        List<VehicleEntity> vehicleList = repository.findAllByOwnerId(ownerId);
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            Profile profile = new Profile(vehicleEntity.getOwner().getId());

            List<VehicleImage> images = new ArrayList<>();

            for(VehicleImage vehicleImage : vehicleEntity.getImages())
            {
                images.add(new VehicleImage(vehicleImage.getId(), vehicleImage.getVehicle(), vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
            }

            vehicles.add(new Vehicle(vehicleEntity.getId(), profile, vehicleEntity.getVin(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity(), images));
        }

        return vehicles;
    }


    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    public Vehicle findByVin(String vin) {
        VehicleEntity vehicleEntity = repository.findByVin(vin);

        List<VehicleImage> images = new ArrayList<>();

        for(VehicleImage vehicleImage : vehicleEntity.getImages())
        {
            images.add(new VehicleImage(vehicleImage.getId(), vehicleImage.getVehicle(), vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
        }

        return new Vehicle(vehicleEntity.getId(), vehicleEntity.getOwner(), vehicleEntity.getVin(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity(), images);
    }

    public Boolean existsByVin(String vin)
    {
        return repository.existsByVin(vin);
    }

    public void addNewImage(Vehicle vehicle, VehicleImage vehicleImage)
    {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicle.getId());
        VehicleImageEntity vehicleImageEntity = new VehicleImageEntity(vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt());

        vehicleEntity.addImage(vehicleImageEntity);
    }

    public void removeImage(VehicleImage vehicleImage)
    {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicleImage.getVehicle().getId());

        VehicleImageEntity imageEntity = new VehicleImageEntity(vehicleImage.getId(), vehicleEntity, vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt());

        vehicleEntity.removeImage(imageEntity);
    }
}
