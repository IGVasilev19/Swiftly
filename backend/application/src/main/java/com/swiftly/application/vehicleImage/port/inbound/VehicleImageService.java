package com.swiftly.application.vehicleImage.port.inbound;

import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;

import java.util.List;

public interface VehicleImageService {
    VehicleImage create(VehicleImage vehicleImage);
    List<VehicleImage> getAllByVehicleId(Integer vehicleId);
}
