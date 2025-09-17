package com.vehiclerental.business;

import com.vehiclerental.controllers.dto.VehicleRequest;
import com.vehiclerental.controllers.dto.VehicleResponse;

import java.util.List;

public interface VehicleService {

    public VehicleResponse create(VehicleRequest req);
    public VehicleResponse update(Integer id, VehicleRequest req);
    public List<VehicleResponse> listAll();
    public VehicleResponse getById(Integer id);
    public void delete(Integer id);
}
