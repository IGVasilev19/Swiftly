package com.vehiclerental.business.impl;

import com.vehiclerental.business.VehicleService;
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

    public void create(VehicleEntity v)
    {
        VehicleEntity entity = new VehicleEntity();
        entity.setVIN(v.getVIN());
        entity.setColor(v.getColor());

        VehicleEntity saved = repo.save(entity);
    }

    public void update(Integer id, VehicleEntity entity)
    {
        VehicleEntity vehicle = repo.findById(id);

        if(entity == null)
        {
            throw new IllegalArgumentException("Vehicle not found: " + id);
        }

        vehicle.setVIN(entity.getVIN());
        vehicle.setColor(entity.getColor());

        VehicleEntity saved = repo.update(vehicle);
    }

    public List<VehicleEntity> listAll() {
        return repo.getAll();
    }
    public VehicleEntity getById(Integer id)
    {
        VehicleEntity vehicle = repo.findById(id);

        if(vehicle == null)
        {
           throw new IllegalArgumentException("Vehicle not found: " + id);
        }

        return vehicle;
    }

    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}