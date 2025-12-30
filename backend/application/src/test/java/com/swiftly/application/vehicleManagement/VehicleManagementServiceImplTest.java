package com.swiftly.application.vehicleManagement;

import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class VehicleManagementServiceImplTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ProfileService profileService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VehicleManagementServiceImpl vehicleManagementService;

    private User testOwner;
    
    @BeforeEach
    void setUp() {
        testOwner = new User();
        testOwner.setId(1);
        testOwner.setEmail("owner@example.com");
        testOwner.setRoles(List.of(Role.OWNER));
    }

    @Test
    void addVehicle_WithImages_ShouldCreateVehicleAndImages() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);
        when(profileService.getById(1)).thenReturn(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        MultipartFile testImageFile = mock(MultipartFile.class);
        when(testImageFile.getBytes()).thenReturn("image-data".getBytes());
        when(testImageFile.getContentType()).thenReturn("image/jpeg");
        when(testImageFile.getOriginalFilename()).thenReturn("test-image.jpg");

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");
        vehicleToCreate.setMake("Toyota");
        vehicleToCreate.setModel("Camry");

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        savedVehicle.setVin("VIN123456789");
        savedVehicle.setMake("Toyota");
        savedVehicle.setModel("Camry");
        savedVehicle.setOwner(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleManagementService.addVehicle(vehicleToCreate, List.of(testImageFile));

        assertNotNull(result);
        verify(vehicleService).create(any(Vehicle.class));
        verify(vehicleRepository).addNewImage(eq(savedVehicle), any(VehicleImage.class));
    }

    @Test
    void addVehicle_WithMultipleImages_ShouldCreateAllImages() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);
        when(profileService.getById(1)).thenReturn(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        MultipartFile testImageFile = mock(MultipartFile.class);
        when(testImageFile.getBytes()).thenReturn("image-data".getBytes());
        when(testImageFile.getContentType()).thenReturn("image/jpeg");
        when(testImageFile.getOriginalFilename()).thenReturn("test-image.jpg");

        MultipartFile imageFile2 = mock(MultipartFile.class);
        when(imageFile2.getBytes()).thenReturn("image-data-2".getBytes());
        when(imageFile2.getContentType()).thenReturn("image/png");
        when(imageFile2.getOriginalFilename()).thenReturn("test-image-2.png");

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        savedVehicle.setOwner(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleManagementService.addVehicle(vehicleToCreate, 
                Arrays.asList(testImageFile, imageFile2));

        assertNotNull(result);
        verify(vehicleRepository, times(2)).addNewImage(eq(savedVehicle), any(VehicleImage.class));
    }

    @Test
    void addVehicle_IOException_ShouldThrowRuntimeException() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);
        when(profileService.getById(1)).thenReturn(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        savedVehicle.setOwner(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));
        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);

        MultipartFile faultyFile = mock(MultipartFile.class);
        when(faultyFile.getBytes()).thenThrow(new IOException("File read error"));

        assertThrows(IllegalArgumentException.class, 
                () -> vehicleManagementService.addVehicle(vehicleToCreate, List.of(faultyFile)));
    }

    @Test
    void addImage_ShouldCallRepository() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        VehicleImage image = new VehicleImage();
        image.setId(1);

        vehicleManagementService.addImage(vehicle, image);

        verify(vehicleRepository).addNewImage(vehicle, image);
    }

    @Test
    void deleteImage_ShouldCallRepository() {
        VehicleImage image = new VehicleImage();
        image.setId(1);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        image.setVehicle(vehicle);

        vehicleManagementService.deleteImage(image);

        verify(vehicleRepository).removeImage(image);
    }

    @Test
    void addVehicle_WithEmptyImages_ShouldCreateVehicleWithoutImages() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);
        when(profileService.getById(1)).thenReturn(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        savedVehicle.setOwner(new com.swiftly.domain.Profile(1, testOwner, "Name", "123", "url"));

        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleManagementService.addVehicle(vehicleToCreate, List.of());

        assertNotNull(result);
        verify(vehicleService).create(any(Vehicle.class));
        verify(vehicleRepository, never()).addNewImage(any(Vehicle.class), any(VehicleImage.class));
    }
}

