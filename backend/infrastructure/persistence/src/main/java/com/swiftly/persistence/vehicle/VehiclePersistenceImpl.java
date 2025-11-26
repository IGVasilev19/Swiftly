package com.swiftly.persistence.vehicle;

import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Vehicle;
import com.swiftly.persistence.entities.VehicleEntity;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehiclePersistenceImpl implements VehicleRepository {
    private final JpaVehicleRepository repository;

    public Vehicle save(Vehicle vehicle)
    {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicle.getOwner(), vehicle.getVIN(), vehicle.getMake(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear(), vehicle.getType(), vehicle.getFuelType(), vehicle.getFuelConsumption(), vehicle.getFeatures(), vehicle.getCountry(), vehicle.getCity());
        return repository.save(vehicleEntity);
    }

    public Vehicle findById(Integer id)
    {
        Optional<VehicleEntity> vehicleEntity = repository.findById(id);

        return new Vehicle(vehicleEntity.get().getOwner(), vehicleEntity.get().getVIN(), vehicleEntity.get().getMake(),vehicleEntity.get().getModel(), vehicleEntity.get().getColor(), vehicleEntity.get().getYear(), vehicleEntity.get().getType(), vehicleEntity.get().getFuelType(), vehicleEntity.get().getFuelConsumption(), vehicleEntity.get().getFeatures(), vehicleEntity.get().getCountry(), vehicleEntity.get().getCity());
    }


    public List<Vehicle> findAll() {
        List<VehicleEntity> vehicleList = repository.findAll();
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            vehicles.add(new Vehicle(vehicleEntity.getId(), vehicleEntity.getOwner(), vehicleEntity.getVIN(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity()));
        }

        return vehicles;
    }


    public List<Vehicle> findAllByOwnerId(Integer ownerId) {
        List<VehicleEntity> vehicleList = repository.findAllByOwnerId(ownerId);
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            vehicles.add(new Vehicle(vehicleEntity.getId(), vehicleEntity.getOwner(), vehicleEntity.getVIN(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity()));
        }

        return vehicles;
    }


    public List<Vehicle> findAllByRenterId(Integer renterId) {
        List<VehicleEntity> vehicleList = repository.findAllByRenterId(renterId);
        List<Vehicle> vehicles = new ArrayList<>();

        for (VehicleEntity vehicleEntity : vehicleList)
        {
            vehicles.add(new Vehicle(vehicleEntity.getId(), vehicleEntity.getOwner(), vehicleEntity.getVIN(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity()));
        }

        return vehicles;
    }


    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    public Vehicle findByVin(String vin) {
        VehicleEntity vehicleEntity = repository.findByVin(vin);

        return new Vehicle(vehicleEntity.getId(), vehicleEntity.getOwner(), vehicleEntity.getVIN(), vehicleEntity.getMake(),vehicleEntity.getModel(), vehicleEntity.getColor(), vehicleEntity.getYear(), vehicleEntity.getType(), vehicleEntity.getFuelType(), vehicleEntity.getFuelConsumption(), vehicleEntity.getFeatures(), vehicleEntity.getCountry(), vehicleEntity.getCity());
    }

    public void update(Vehicle vehicle) {

    }

}
