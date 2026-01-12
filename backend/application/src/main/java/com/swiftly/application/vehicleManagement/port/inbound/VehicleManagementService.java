package com.swiftly.application.vehicleManagement.port.inbound;

import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleManagementService {
    Vehicle addVehicle(Vehicle vehicle, List<MultipartFile> images);
    void addImage(Vehicle vehicle, VehicleImage image);
    void deleteImage(VehicleImage vehicleImage);
    void updateVehicle(Integer id,Vehicle vehicle, List<MultipartFile> images);
    void deleteListedVehicle(Integer id);
}
