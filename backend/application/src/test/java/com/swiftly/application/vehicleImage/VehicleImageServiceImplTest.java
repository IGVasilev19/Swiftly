package com.swiftly.application.vehicleImage;

import com.swiftly.application.vehicleImage.port.outbound.VehicleImageRepository;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class VehicleImageServiceImplTest {

    @Mock
    private VehicleImageRepository repository;

    @InjectMocks
    private VehicleImageServiceImpl vehicleImageService;

    private Vehicle testVehicle;
    private VehicleImage testImage;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle();
        testVehicle.setId(1);
        testVehicle.setVin("VIN123456789");

        testImage = new VehicleImage();
        testImage.setId(1);
        testImage.setVehicle(testVehicle);
        testImage.setData("image-data".getBytes());
        testImage.setMimeType("image/jpeg");
        testImage.setFileName("test-image.jpg");
        testImage.setUploadedAt(LocalDateTime.now());
    }

    @Test
    void create_ShouldSaveAndReturnVehicleImage() {
        when(repository.save(any(VehicleImage.class))).thenReturn(testImage);

        VehicleImage result = vehicleImageService.create(testImage);

        assertNotNull(result);
        assertEquals(testImage, result);
        verify(repository).save(testImage);
    }

    @Test
    void getAllByVehicleId_ShouldReturnVehicleImages() {
        VehicleImage image2 = new VehicleImage();
        image2.setId(2);
        image2.setVehicle(testVehicle);
        image2.setData("image-data-2".getBytes());
        image2.setMimeType("image/png");
        image2.setFileName("test-image-2.png");
        image2.setUploadedAt(LocalDateTime.now());

        List<VehicleImage> images = Arrays.asList(testImage, image2);
        when(repository.findAllByVehicleId(1)).thenReturn(images);

        List<VehicleImage> result = vehicleImageService.getAllByVehicleId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllByVehicleId(1);
    }

    @Test
    void getAllByVehicleId_NoImages_ShouldReturnEmptyList() {
        when(repository.findAllByVehicleId(1)).thenReturn(List.of());

        List<VehicleImage> result = vehicleImageService.getAllByVehicleId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllByVehicleId(1);
    }
}

