package com.swiftly.application.vehicleManagement;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicleImage.port.inbound.VehicleImageService;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
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
    private final ListingService listingService;

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle, List<MultipartFile> images) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

        List<VehicleImage> images = vehicleImageService.getAllByVehicleId(id);

        existingVehicle.setImages(images);

        return existingVehicle;
    }

    public Boolean vehicleHasListing(Integer id)
    {
        return listingService.checkExistsByVehicleId(id);
    }
}
