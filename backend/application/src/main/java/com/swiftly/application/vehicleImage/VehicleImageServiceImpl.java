package com.swiftly.application.vehicleImage;

import com.swiftly.application.vehicleImage.port.inbound.VehicleImageService;
import com.swiftly.application.vehicleImage.port.outbound.VehicleImageRepository;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class VehicleImageServiceImpl implements VehicleImageService {
    private final VehicleImageRepository repository;

    public VehicleImage create(VehicleImage vehicleImage) {
        return repository.save(vehicleImage);
    }

    public List<VehicleImage> getAllByVehicleId(Integer vehicleId)
    {
        return repository.findAllByVehicleId(vehicleId);
    }
}
