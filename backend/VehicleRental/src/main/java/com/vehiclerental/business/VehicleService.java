package com.vehiclerental.business;

import com.vehiclerental.controllers.dto.VehicleRequest;
import com.vehiclerental.controllers.dto.VehicleResponse;
import com.vehiclerental.persistence.entity.VehicleEntity;

import java.util.List;

public interface VehicleService {

    public void create(VehicleEntity v);
    public void update(Integer id, VehicleEntity v);
    public List<VehicleEntity> listAll();
    public VehicleEntity getById(Integer id);
    public void delete(Integer id);
}
