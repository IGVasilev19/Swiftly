package com.swiftly.application.vehicleManagement;

import com.swiftly.application.listing.inbound.ListingService;
import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.application.vehicleManagement.port.inbound.VehicleManagementService;
import com.swiftly.domain.*;
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
    private final ListingService listingService;

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
        Vehicle vehicleForImages = vehicleService.getById(id);
        vehicle.setId(id);
        vehicle.setOwner(vehicleForImages.getOwner());
        vehicle.setIsRemoved(vehicleForImages.getIsRemoved());

        if (images != null && !images.isEmpty())
        {

            List<VehicleImage> existingImages = vehicleForImages.getImages();
            if (existingImages != null && !existingImages.isEmpty()) {
                List<VehicleImage> imagesToDelete = new ArrayList<>(existingImages);
                for (VehicleImage existingImage : imagesToDelete) {
                    deleteImage(existingImage);
                }
            }
            for (MultipartFile image : images) {
                try {
                    VehicleImage vehicleImage = new VehicleImage(
                            null,
                            vehicleForImages,
                            image.getBytes(),
                            image.getContentType(),
                            image.getOriginalFilename(),
                            LocalDateTime.now()
                    );
                    addImage(vehicleForImages, vehicleImage);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Could not load vehicle image file", e);
                }
            }
        }

        vehicleService.updateVehicle(vehicle);
    }

    @Transactional
    public void deleteListedVehicle(Integer id)
    {
        vehicleService.removeById(id);

        Listing listing = listingService.getByVehicleId(id);
        listingService.removeById(listing.getId());
    }
}
