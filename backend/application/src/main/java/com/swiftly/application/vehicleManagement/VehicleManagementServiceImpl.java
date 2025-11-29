package com.swiftly.application.vehicleManagement;

import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicleImage.port.inbound.VehicleImageService;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleManagementServiceImpl implements VehicleManagementService {
    private final VehicleService vehicleService;
    private final VehicleImageService vehicleImageService;

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle) {
        Vehicle newVehicle = vehicleService.create(vehicle);

        List<VehicleImage> newImages = new ArrayList<>();

        for(VehicleImage image : vehicle.getImages())
        {
            newImages.add(vehicleImageService.create(image));
        }

        newVehicle.setImages(newImages);

        return newVehicle;
    }
}
