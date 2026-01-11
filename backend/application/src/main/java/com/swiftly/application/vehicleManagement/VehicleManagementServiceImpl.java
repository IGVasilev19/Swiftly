package com.swiftly.application.vehicleManagement;

import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
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

@RequiredArgsConstructor
@Service
@Transactional
public class VehicleManagementServiceImpl implements VehicleManagementService {
    private final VehicleService vehicleService;
    private final ProfileService profileService;
    private final VehicleRepository vehicleRepository;

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
                        throw new IllegalArgumentException("Could not load vehicle image file");
                    }
                })
                .toList();

        List<VehicleImage> newVehicleImages = new ArrayList<>();

        for (VehicleImage vehicleImage : vehicleImages)
        {
            addImage(newVehicle, vehicleImage);
        }

        newVehicle.setImages(newVehicleImages);

        return newVehicle;
    }

    public void addImage(Vehicle vehicle, VehicleImage image)
    {

            vehicleRepository.addNewImage(vehicle, image);
    }

    public void deleteImage(VehicleImage vehicleImage)
    {
        vehicleRepository.removeImage(vehicleImage);
    }

    @Transactional
    public void updateVehicle(Integer id, Vehicle vehicle, List<MultipartFile> images) {
        Vehicle vehicleToUpdate = vehicleRepository.findById(id);

        if (images != null)
        {
            vehicleToUpdate.getImages().clear();

            for (MultipartFile image : images) {
                try {
                    VehicleImage vehicleImage = new VehicleImage(
                            null,
                            vehicleToUpdate,
                            image.getBytes(),
                            image.getContentType(),
                            image.getOriginalFilename(),
                            LocalDateTime.now()
                    );
                    addImage(vehicleToUpdate, vehicleImage);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Could not load vehicle image file", e);
                }
            }
        }

        vehicleService.updateVehicle(vehicleToUpdate);
    }
}
