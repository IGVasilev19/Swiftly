package com.swiftly.application.vehicleImage.port.outbound;

import com.swiftly.domain.VehicleImage;

public interface VehicleImageRepository {
    VehicleImage save(VehicleImage vehicleImage);
}
