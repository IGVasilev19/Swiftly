package com.vehiclerental.business.impl;

import com.vehiclerental.business.VehicleService;
import com.vehiclerental.controllers.dto.VehicleRequest;
import com.vehiclerental.controllers.dto.VehicleResponse;
import com.vehiclerental.controllers.mapper.VehicleDtoMapper;
import com.vehiclerental.persistence.VehicleRepository;
import com.vehiclerental.persistence.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService
{
    private final VehicleRepository repo;
    private final VehicleDtoMapper mapper;

    public VehicleResponse create(VehicleRequest req)
    {
        VehicleEntity entity = new VehicleEntity();
        entity.setVIN(req.vin());
        entity.setColor(req.color());

        VehicleEntity saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    public VehicleResponse update(Integer id, VehicleRequest req)
    {
        VehicleEntity entity = repo.findById(id);

        if(entity == null)
        {
            throw new IllegalArgumentException("Vehicle not found: " + id);
        }

        entity.setVIN(req.vin());
        entity.setColor(req.color());

        VehicleEntity saved = repo.update(entity);
        return mapper.toResponse(saved);
    }

    public List<VehicleResponse> listAll()
    {
        return repo.getAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public VehicleResponse getById(Integer id)
    {
        VehicleEntity vehicle = repo.findById(id);

        if(vehicle == null)
        {
           throw new IllegalArgumentException("Vehicle not found: " + id);
        }

        return mapper.toResponse(vehicle);
    }

    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}