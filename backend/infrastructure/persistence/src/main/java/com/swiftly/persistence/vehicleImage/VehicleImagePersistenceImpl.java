package com.swiftly.persistence.vehicleImage;

import com.swiftly.application.vehicleImage.port.outbound.VehicleImageRepository;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.entities.VehicleImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public List<VehicleImage> findAllByVehicleId(Integer vehicleId)
    {
        List<VehicleImage> imagesToReturn = new ArrayList<>();

        List<VehicleImageEntity> existingImages = repository.findAllByVehicleId(vehicleId);

        for (VehicleImageEntity existingImage : existingImages)
        {
            Vehicle existingImageVehicle = new Vehicle(existingImage.getVehicle().getId());
            imagesToReturn.add(new VehicleImage(existingImage.getId(),existingImageVehicle,existingImage.getData(),existingImage.getMimeType(),existingImage.getFileName(),existingImage.getUploadedAt()));
        }

        return imagesToReturn;
    }
}
