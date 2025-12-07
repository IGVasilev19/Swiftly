package com.swiftly.application.vehicle;

import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle testVehicle;
    private User testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new User();
        testOwner.setId(1);
        testOwner.setEmail("owner@example.com");
        testOwner.setRoles(List.of(Role.OWNER));

        testVehicle = new Vehicle();
        testVehicle.setId(1);
        testVehicle.setVin("VIN123456789");
        testVehicle.setMake("Toyota");
        testVehicle.setModel("Camry");
        testVehicle.setYear(2020);
        testVehicle.setType(VehicleType.CAR);
        testVehicle.setFuelType(FuelType.PETROL);
        testVehicle.setOwner(testOwner);
    }

    @Test
    void create_NewVehicle_ShouldSaveAndReturnVehicle() {
        when(repository.existsByVin("VIN123456789")).thenReturn(false);
        when(repository.save(testVehicle)).thenReturn(testVehicle);

        Vehicle result = vehicleService.create(testVehicle);

        assertNotNull(result);
        assertEquals(testVehicle, result);
        verify(repository).existsByVin("VIN123456789");
        verify(repository).save(testVehicle);
    }

    @Test
    void create_DuplicateVin_ShouldThrowException() {
        when(repository.existsByVin("VIN123456789")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> vehicleService.create(testVehicle));

        assertEquals("Vehicle already exists", ex.getMessage());
        verify(repository).existsByVin("VIN123456789");
        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void getById_ExistingVehicle_ShouldReturnVehicle() {
        when(repository.findById(1)).thenReturn(testVehicle);

        Vehicle result = vehicleService.getById(1);

        assertNotNull(result);
        assertEquals(testVehicle, result);
        verify(repository).findById(1);
    }

    @Test
    void getAll_ShouldReturnAllVehicles() {
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(2);
        vehicle2.setVin("VIN987654321");
        
        List<Vehicle> vehicles = Arrays.asList(testVehicle, vehicle2);
        when(repository.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void getAllByOwnerId_ShouldReturnOwnerVehicles() {
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(2);
        vehicle2.setVin("VIN987654321");
        vehicle2.setOwner(testOwner);
        
        List<Vehicle> ownerVehicles = Arrays.asList(testVehicle, vehicle2);
        when(repository.findAllByOwnerId(1)).thenReturn(ownerVehicles);

        List<Vehicle> result = vehicleService.getAllByOwnerId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllByOwnerId(1);
    }

    @Test
    void getAllByOwnerId_NoVehicles_ShouldReturnEmptyList() {
        when(repository.findAllByOwnerId(1)).thenReturn(List.of());

        List<Vehicle> result = vehicleService.getAllByOwnerId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllByOwnerId(1);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        vehicleService.deleteById(1);

        verify(repository).deleteById(1);
    }

    @Test
    void getByVin_ExistingVin_ShouldReturnVehicle() {
        when(repository.findByVin("VIN123456789")).thenReturn(testVehicle);

        Vehicle result = vehicleService.getByVin("VIN123456789");

        assertNotNull(result);
        assertEquals(testVehicle, result);
        verify(repository).findByVin("VIN123456789");
    }

    @Test
    void getByVin_NonExistentVin_ShouldReturnNull() {
        when(repository.findByVin("NONEXISTENT")).thenReturn(null);

        Vehicle result = vehicleService.getByVin("NONEXISTENT");

        assertNull(result);
        verify(repository).findByVin("NONEXISTENT");
    }
}

