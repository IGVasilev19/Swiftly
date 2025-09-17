package com.vehiclerental.controllers.mapper;

import com.vehiclerental.controllers.dto.VehicleRequest;
import com.vehiclerental.controllers.dto.VehicleResponse;
import com.vehiclerental.persistence.VehicleRepository;
import com.vehiclerental.persistence.entity.VehicleEntity;
import org.springframework.stereotype.Component;

@Component
public class VehicleDtoMapper {
    private final VehicleRepository vehicleRepository;

    public VehicleDtoMapper(VehicleRepository vehicleRepository)
    {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleEntity toDomain(VehicleRequest req) {
        VehicleEntity newEntity = new VehicleEntity();

        newEntity.setVIN(req.vin());
        newEntity.setColor(req.color());

        return newEntity;
    }

    public VehicleResponse toResponse(VehicleEntity v) {
        return new VehicleResponse(
                v.getId(),
                v.getVIN(),
                v.getColor()
        );
    }
}
