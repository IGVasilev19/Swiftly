package com.vehiclerental.controllers;

import com.vehiclerental.business.VehicleService;
import com.vehiclerental.controllers.dto.VehicleRequest;
import com.vehiclerental.controllers.dto.VehicleResponse;
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
    public List<VehicleResponse> getAll()
    {
        return vehicleService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> get(@PathVariable Integer id)
    {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PostMapping
    public VehicleResponse create(@RequestBody VehicleRequest vehicleRequest)
    {
        return vehicleService.create(vehicleRequest);
    }

    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Integer id, @RequestBody VehicleRequest vehicleRequest)
    {
        return vehicleService.update(id, vehicleRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
