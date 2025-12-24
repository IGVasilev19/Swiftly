package com.swiftly.application.vehicleManagement;

import com.swiftly.application.vehicle.port.inbound.VehicleService;
import com.swiftly.domain.User;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
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
import java.time.LocalDateTime;
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
    private VehicleImageService vehicleImageService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VehicleManagementServiceImpl vehicleManagementService;

    private User testOwner;
    private Vehicle testVehicle;

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
    }

    @Test
    void addVehicle_WithImages_ShouldCreateVehicleAndImages() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);

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
        savedVehicle.setOwner(testOwner);

        VehicleImage savedImage = new VehicleImage();
        savedImage.setId(1);
        savedImage.setVehicle(savedVehicle);
        savedImage.setData("image-data".getBytes());
        savedImage.setMimeType("image/jpeg");
        savedImage.setFileName("test-image.jpg");
        savedImage.setUploadedAt(LocalDateTime.now());

        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);
        when(vehicleImageService.create(any(VehicleImage.class))).thenReturn(savedImage);

        Vehicle result = vehicleManagementService.addVehicle(vehicleToCreate, List.of(testImageFile));

        assertNotNull(result);
        assertEquals(testOwner, result.getOwner());
        assertNotNull(result.getImages());
        assertEquals(1, result.getImages().size());
        verify(vehicleService).create(any(Vehicle.class));
        verify(vehicleImageService).create(any(VehicleImage.class));
    }

    @Test
    void addVehicle_WithMultipleImages_ShouldCreateAllImages() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);

        MultipartFile testImageFile = mock(MultipartFile.class);
        when(testImageFile.getBytes()).thenReturn("image-data".getBytes());
        when(testImageFile.getContentType()).thenReturn("image/jpeg");
        when(testImageFile.getOriginalFilename()).thenReturn("test-image.jpg");

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        savedVehicle.setOwner(testOwner);

        MultipartFile imageFile2 = mock(MultipartFile.class);
        when(imageFile2.getBytes()).thenReturn("image-data-2".getBytes());
        when(imageFile2.getContentType()).thenReturn("image/png");
        when(imageFile2.getOriginalFilename()).thenReturn("test-image-2.png");

        VehicleImage savedImage1 = new VehicleImage();
        savedImage1.setId(1);
        savedImage1.setVehicle(savedVehicle);

        VehicleImage savedImage2 = new VehicleImage();
        savedImage2.setId(2);
        savedImage2.setVehicle(savedVehicle);

        when(vehicleService.create(any(Vehicle.class))).thenReturn(savedVehicle);
        when(vehicleImageService.create(any(VehicleImage.class)))
                .thenReturn(savedImage1)
                .thenReturn(savedImage2);

        Vehicle result = vehicleManagementService.addVehicle(vehicleToCreate, 
                Arrays.asList(testImageFile, imageFile2));

        assertNotNull(result);
        assertEquals(2, result.getImages().size());
        verify(vehicleImageService, times(2)).create(any(VehicleImage.class));
    }

    @Test
    void addVehicle_IOException_ShouldThrowRuntimeException() throws IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testOwner);

        Vehicle vehicleToCreate = new Vehicle();
        vehicleToCreate.setVin("VIN123456789");

        MultipartFile faultyFile = mock(MultipartFile.class);
        when(faultyFile.getBytes()).thenThrow(new IOException("File read error"));

        assertThrows(RuntimeException.class, 
                () -> vehicleManagementService.addVehicle(vehicleToCreate, List.of(faultyFile)));
    }

    @Test
    void getFullVehicleById_WithImages_ShouldReturnVehicleWithImages() {
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(1);
        existingVehicle.setVin("VIN123456789");

        VehicleImage image1 = new VehicleImage();
        image1.setId(1);
        image1.setVehicle(existingVehicle);

        VehicleImage image2 = new VehicleImage();
        image2.setId(2);
        image2.setVehicle(existingVehicle);

        when(vehicleService.getById(1)).thenReturn(existingVehicle);
        when(vehicleImageService.getAllByVehicleId(1)).thenReturn(Arrays.asList(image1, image2));

        Vehicle result = vehicleManagementService.getFullVehicleById(1);

        assertNotNull(result);
        assertEquals(existingVehicle, result);
        assertNotNull(result.getImages());
        assertEquals(2, result.getImages().size());
        verify(vehicleService).getById(1);
        verify(vehicleImageService).getAllByVehicleId(1);
    }

    @Test
    void getFullVehicleById_NoImages_ShouldReturnVehicleWithEmptyImages() {
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(1);
        existingVehicle.setVin("VIN123456789");

        when(vehicleService.getById(1)).thenReturn(existingVehicle);
        when(vehicleImageService.getAllByVehicleId(1)).thenReturn(List.of());

        Vehicle result = vehicleManagementService.getFullVehicleById(1);

        assertNotNull(result);
        assertEquals(existingVehicle, result);
        assertNotNull(result.getImages());
        assertTrue(result.getImages().isEmpty());
    }
}

