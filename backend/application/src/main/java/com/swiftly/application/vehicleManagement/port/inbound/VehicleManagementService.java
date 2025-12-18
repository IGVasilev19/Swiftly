package com.swiftly.application.vehicleManagement.port.inbound;

import com.swiftly.domain.Vehicle;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleManagementService {
    Vehicle addVehicle(Vehicle vehicle, List<MultipartFile> images);
    Vehicle getFullVehicleById(Integer id);
    Boolean vehicleHasListing(Integer id);
}
