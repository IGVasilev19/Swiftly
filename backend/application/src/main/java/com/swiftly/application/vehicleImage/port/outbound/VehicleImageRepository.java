package com.swiftly.application.vehicleImage.port.outbound;

import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;

import java.util.List;

public interface VehicleImageRepository {
    VehicleImage save(VehicleImage vehicleImage);
    List<VehicleImage> findAllByVehicleId(Integer vehicleId);
}
