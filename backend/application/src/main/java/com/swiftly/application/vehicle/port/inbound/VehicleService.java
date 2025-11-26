package com.swiftly.application.vehicle.port.inbound;

import com.swiftly.domain.Vehicle;

public interface VehicleService {
    Vehicle getById(Integer id);
}
