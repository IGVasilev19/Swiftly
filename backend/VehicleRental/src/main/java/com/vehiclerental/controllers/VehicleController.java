package com.vehiclerental.controllers;

import com.vehiclerental.business.VehicleService;
import com.vehiclerental.persistence.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private final VehicleService vehicleService;

    @GetMapping
    public List<VehicleEntity> getAll()
    {
        return vehicleService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleEntity> get(@PathVariable Integer id)
    {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PostMapping
    public void create(@RequestBody VehicleEntity vehicle)
    {
        vehicleService.create(vehicle);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody VehicleEntity vehicle)
    {
        vehicleService.update(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        vehicleService.delete(id);
    }
}
