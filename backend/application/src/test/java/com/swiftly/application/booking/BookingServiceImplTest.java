package com.swiftly.application.booking;

import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.domain.Booking;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.booking.Status;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private ProfileService profileService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking testBooking;
    private Listing testListing;
    private Profile testRenter;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("renter@example.com");
        testUser.setRoles(List.of(Role.RENTER));

        testRenter = new Profile();
        testRenter.setId(1);
        testRenter.setFullName("Test Renter");

        testListing = new Listing();
        testListing.setId(1);
        testListing.setInstantBook(false);

        testBooking = new Booking();
        testBooking.setId(1);
        testBooking.setListing(testListing);
        testBooking.setStartAt(LocalDate.now().plusDays(1));
        testBooking.setEndAt(LocalDate.now().plusDays(3));
        testBooking.setTotalPrice(new BigDecimal("200.00"));
    }

    @Test
    void create_NewBookingWithNonInstantBook_ShouldSetStatusRequested() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(profileService.getById(1)).thenReturn(testRenter);
        when(repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(repository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.create(testBooking);

        assertNotNull(result);
        assertEquals(Status.REQUESTED, result.getStatus());
        assertEquals(testRenter, result.getRenter());
        verify(repository).existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(anyInt(), any(LocalDate.class), any(LocalDate.class));
        verify(repository).save(any(Booking.class));
    }

    @Test
    void create_NewBookingWithInstantBook_ShouldSetStatusApproved() {
        testListing.setInstantBook(true);
        testBooking.setListing(testListing);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(profileService.getById(1)).thenReturn(testRenter);
        when(repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(repository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.create(testBooking);

        assertNotNull(result);
        assertEquals(Status.APPROVED, result.getStatus());
        assertEquals(testRenter, result.getRenter());
        verify(repository).save(any(Booking.class));
    }

    @Test
    void create_DuplicateBooking_ShouldThrowException() {
        when(repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.create(testBooking));

        assertEquals("Booking already exists", ex.getMessage());
        verify(repository).existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(anyInt(), any(LocalDate.class), any(LocalDate.class));
        verify(repository, never()).save(any(Booking.class));
    }

    @Test
    void getById_ExistingBooking_ShouldReturnBooking() {
        when(repository.findById(1)).thenReturn(testBooking);

        Booking result = bookingService.getById(1);

        assertNotNull(result);
        assertEquals(testBooking, result);
        verify(repository).findById(1);
    }

    @Test
    void getAllByRenterId_ShouldReturnRenterBookings() {
        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setRenter(testRenter);

        List<Booking> bookings = Arrays.asList(testBooking, booking2);
        when(repository.findAllByRenterId(1)).thenReturn(bookings);

        List<Booking> result = bookingService.getAllByRenterId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllByRenterId(1);
    }

    @Test
    void getAllByRenterId_NoBookings_ShouldReturnEmptyList() {
        when(repository.findAllByRenterId(1)).thenReturn(List.of());

        List<Booking> result = bookingService.getAllByRenterId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllByRenterId(1);
    }

    @Test
    void getAllByListingId_ShouldReturnListingBookings() {
        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setListing(testListing);

        List<Booking> bookings = Arrays.asList(testBooking, booking2);
        when(repository.findAllByListingId(1)).thenReturn(bookings);

        List<Booking> result = bookingService.getAllByListingId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllByListingId(1);
    }

    @Test
    void getAllByListingId_NoBookings_ShouldReturnEmptyList() {
        when(repository.findAllByListingId(1)).thenReturn(List.of());

        List<Booking> result = bookingService.getAllByListingId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllByListingId(1);
    }

    @Test
    void bookingAlreadyExists_ExistingBooking_ShouldReturnTrue() {
        when(repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(1, testBooking.getEndAt(), testBooking.getStartAt())).thenReturn(true);

        Boolean result = bookingService.bookingAlreadyExists(1, testBooking.getEndAt(), testBooking.getStartAt());

        assertTrue(result);
        verify(repository).existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(1, testBooking.getEndAt(), testBooking.getStartAt());
    }

    @Test
    void bookingAlreadyExists_NoBooking_ShouldReturnFalse() {
        when(repository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(1, testBooking.getEndAt(), testBooking.getStartAt())).thenReturn(false);

        Boolean result = bookingService.bookingAlreadyExists(1, testBooking.getEndAt(), testBooking.getStartAt());

        assertFalse(result);
        verify(repository).existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(1, testBooking.getEndAt(), testBooking.getStartAt());
    }

    @Test
    void getAllByListingVehicleOwnerId_ShouldReturnOwnerBookings() {
        Booking booking2 = new Booking();
        booking2.setId(2);

        List<Booking> bookings = Arrays.asList(testBooking, booking2);
        when(repository.findAllByListingVehicleOwnerId(1)).thenReturn(bookings);

        List<Booking> result = bookingService.getAllByListingVehicleOwnerId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAllByListingVehicleOwnerId(1);
    }

    @Test
    void getAllByListingVehicleOwnerId_NoBookings_ShouldReturnEmptyList() {
        when(repository.findAllByListingVehicleOwnerId(1)).thenReturn(List.of());

        List<Booking> result = bookingService.getAllByListingVehicleOwnerId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllByListingVehicleOwnerId(1);
    }
}

