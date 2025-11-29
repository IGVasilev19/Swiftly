package com.swiftly.persistence.vehicleImage;

import com.swiftly.application.vehicleImage.port.outbound.VehicleImageRepository;
import com.swiftly.domain.VehicleImage;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VehicleImagePersistenceImpl implements VehicleImageRepository {
    private final JpaVehicleImageRepository repository;

    public VehicleImage save(VehicleImage vehicleImage)
    {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicleImage.getVehicle().getId());

        VehicleImageEntity vehicleImageEntity = new VehicleImageEntity(vehicleEntity, vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt());

        return repository.save(vehicleImageEntity);
    }
}
