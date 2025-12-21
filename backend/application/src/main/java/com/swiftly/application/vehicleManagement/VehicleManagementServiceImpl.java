package com.swiftly.application.vehicleManagement;

import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicleImage.port.inbound.VehicleImageService;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleManagementServiceImpl implements VehicleManagementService {
    private final VehicleService vehicleService;
    private final VehicleImageService vehicleImageService;
    private final ProfileService profileService;

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle, List<MultipartFile> images) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile owner = profileService.getById(loggedUser.getId());
        vehicle.setOwner(owner);

        Vehicle newVehicle = vehicleService.create(vehicle);

        List<VehicleImage> vehicleImages = images.stream()
                .map(file -> {
                    try {
                        return new VehicleImage(
                                null,
                                vehicle,
                                file.getBytes(),
                                file.getContentType(),
                                file.getOriginalFilename(),
                                LocalDateTime.now()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        List<VehicleImage> newVehicleImages = new ArrayList<>();

        for (VehicleImage vehicleImage : vehicleImages)
        {
            vehicleImage.setVehicle(newVehicle);
            newVehicleImages.add(vehicleImageService.create(vehicleImage));
        }

        newVehicle.setImages(newVehicleImages);

        return newVehicle;
    }

    @Transactional
    public Vehicle getFullVehicleById(Integer id)
    {
        Vehicle existingVehicle = vehicleService.getById(id);

        populateVehicleWithImages(existingVehicle);

        return existingVehicle;
    }

    public void populateVehicleWithImages(Vehicle vehicle)
    {
        List<VehicleImage> images = vehicleImageService.getAllByVehicleId(vehicle.getId());

        vehicle.setImages(images);
    }
}
