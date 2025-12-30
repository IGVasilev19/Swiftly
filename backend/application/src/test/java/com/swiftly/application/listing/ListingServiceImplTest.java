package com.swiftly.application.listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class ListingServiceImplTest {

    @Mock
    private ListingRepository repository;

    @InjectMocks
    private ListingServiceImpl listingService;

    private Listing testListing;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle();
        testVehicle.setId(1);
        testVehicle.setVin("VIN123456789");

        testListing = new Listing();
        testListing.setId(1);
        testListing.setVehicle(testVehicle);
        testListing.setTitle("Test Listing");
        testListing.setDescription("Test Description");
        testListing.setBasePricePerDay(new BigDecimal("100.00"));
        testListing.setInstantBook(true);
    }

    @Test
    void create_NewListing_ShouldSaveAndReturnListing() {
        when(repository.existsByVehicleId(1)).thenReturn(false);
        when(repository.save(any(Listing.class))).thenReturn(testListing);

        Listing result = listingService.create(testListing);

        assertNotNull(result);
        assertEquals(testListing, result);
        verify(repository).existsByVehicleId(1);
        verify(repository).save(any(Listing.class));
    }

    @Test
    void create_DuplicateVehicleId_ShouldThrowException() {
        when(repository.existsByVehicleId(1)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> listingService.create(testListing));

        assertEquals("Listing already exists for this vehicle", ex.getMessage());
        verify(repository).existsByVehicleId(1);
        verify(repository, never()).save(any(Listing.class));
    }

    @Test
    void getAll_ShouldReturnAllListings() {
        Listing listing2 = new Listing();
        listing2.setId(2);
        listing2.setTitle("Second Listing");

        List<Listing> listings = Arrays.asList(testListing, listing2);
        when(repository.findAllWithVehicle()).thenReturn(listings);

        List<Listing> result = listingService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllWithVehicle();
    }

    @Test
    void getAll_NoListings_ShouldReturnEmptyList() {
        when(repository.findAllWithVehicle()).thenReturn(List.of());

        List<Listing> result = listingService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllWithVehicle();
    }

    @Test
    void getById_ExistingListing_ShouldReturnListing() {
        when(repository.findById(1)).thenReturn(testListing);

        Listing result = listingService.getById(1);

        assertNotNull(result);
        assertEquals(testListing, result);
        verify(repository).findById(1);
    }

    @Test
    void getByVehicleId_ExistingVehicle_ShouldReturnListing() {
        when(repository.findByVehicleId(1)).thenReturn(testListing);

        Listing result = listingService.getByVehicleId(1);

        assertNotNull(result);
        assertEquals(testListing, result);
        verify(repository).findByVehicleId(1);
    }

    @Test
    void checkExistsByVehicleId_ExistingVehicle_ShouldReturnTrue() {
        when(repository.existsByVehicleId(1)).thenReturn(true);

        Boolean result = listingService.checkExistsByVehicleId(1);

        assertTrue(result);
        verify(repository).existsByVehicleId(1);
    }

    @Test
    void checkExistsByVehicleId_NonExistentVehicle_ShouldReturnFalse() {
        when(repository.existsByVehicleId(999)).thenReturn(false);

        Boolean result = listingService.checkExistsByVehicleId(999);

        assertFalse(result);
        verify(repository).existsByVehicleId(999);
    }
}

