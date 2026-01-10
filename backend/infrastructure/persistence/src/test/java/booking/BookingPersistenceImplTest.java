package booking;

import com.swiftly.application.booking.port.outbound.BookingRepository;
import com.swiftly.domain.Booking;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.booking.Status;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.booking.BookingPersistenceImpl;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.BookingEntity;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import({BookingPersistenceImpl.class, com.swiftly.persistence.helpers.HelperImpl.class})
class BookingPersistenceImplTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity ownerUser;
    private UserEntity renterUser;
    private ProfileEntity owner;
    private ProfileEntity renter;
    private VehicleEntity vehicle;
    private ListingEntity listing;
    private Profile ownerDomain;
    private Profile renterDomain;
    private Vehicle vehicleDomain;
    private Listing listingDomain;

    @BeforeEach
    void setUp() {
        ownerUser = new UserEntity("owner@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(ownerUser);
        owner = new ProfileEntity(ownerUser, "Owner Name", "+1234567890", null);
        entityManager.persistAndFlush(owner);

        renterUser = new UserEntity("renter@test.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(renterUser);
        renter = new ProfileEntity(renterUser, "Renter Name", "+0987654321", null);
        entityManager.persistAndFlush(renter);

        vehicle = new VehicleEntity();
        vehicle.setOwner(owner);
        vehicle.setVin("1HGBH41JXMN109186");
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setColor("Blue");
        vehicle.setYear(2020);
        vehicle.setType(VehicleType.CAR);
        vehicle.setFuelType(FuelType.PETROL);
        vehicle.setFuelConsumption(8.5);
        vehicle.setFeatures(List.of());
        vehicle.setCountry("Netherlands");
        vehicle.setCity("Amsterdam");
        entityManager.persistAndFlush(vehicle);

        listing = new ListingEntity(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listing);

        ownerDomain = new Profile(owner.getId(), owner.getFullName(), owner.getPhone(), owner.getAvatarUrl());
        renterDomain = new Profile(renter.getId(), renter.getFullName(), renter.getPhone(), renter.getAvatarUrl());
        vehicleDomain = new Vehicle(vehicle.getId());
        listingDomain = new Listing(listing.getId());
    }

    @Test
    void save_WhenValidBooking_ShouldPersistAndReturnMappedBooking() {
        Booking booking = new Booking(listingDomain, renterDomain, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));

        Booking saved = bookingRepository.save(booking);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStartAt()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(saved.getEndAt()).isEqualTo(LocalDate.of(2024, 1, 5));
        assertThat(saved.getStatus()).isEqualTo(Status.REQUESTED);
        assertThat(saved.getTotalPrice()).isEqualByComparingTo(new BigDecimal("500.00"));

        BookingEntity entity = entityManager.find(BookingEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getListing().getId()).isEqualTo(listing.getId());
        assertThat(entity.getRenter().getId()).isEqualTo(renter.getId());
    }

    @Test
    void save_WhenListingNotFound_ShouldThrowIllegalArgumentException() {
        Listing nonExistentListing = new Listing(999);
        Booking booking = new Booking(nonExistentListing, renterDomain, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));

        assertThatThrownBy(() -> bookingRepository.save(booking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Listing not found");
    }

    @Test
    void save_WhenRenterNotFound_ShouldThrowIllegalArgumentException() {
        Profile nonExistentRenter = new Profile(999, "Non Existent", "+9999999999", null);
        Booking booking = new Booking(listingDomain, nonExistentRenter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));

        assertThatThrownBy(() -> bookingRepository.save(booking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Renter not found");
    }

    @Test
    void findById_WhenBookingExists_ShouldReturnMappedBooking() {
        BookingEntity bookingEntity = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(bookingEntity);

        Booking found = bookingRepository.findById(bookingEntity.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(bookingEntity.getId());
        assertThat(found.getStartAt()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(found.getEndAt()).isEqualTo(LocalDate.of(2024, 1, 5));
        assertThat(found.getStatus()).isEqualTo(Status.REQUESTED);
        assertThat(found.getTotalPrice()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void findById_WhenBookingDoesNotExist_ShouldThrowException() {
        assertThatThrownBy(() -> bookingRepository.findById(999))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void findAllByRenterId_WhenBookingsExist_ShouldReturnMappedBookings() {
        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<Booking> found = bookingRepository.findAllByRenterId(renter.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Booking::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
        assertThat(found).extracting(Booking::getStatus).containsExactlyInAnyOrder(Status.REQUESTED, Status.APPROVED);
    }

    @Test
    void findAllByRenterId_WhenNoBookingsExist_ShouldReturnEmptyList() {
        List<Booking> found = bookingRepository.findAllByRenterId(renter.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByListingId_WhenBookingsExist_ShouldReturnMappedBookings() {
        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<Booking> found = bookingRepository.findAllByListingId(listing.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Booking::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void findAllByListingId_WhenNoBookingsExist_ShouldReturnEmptyList() {
        List<Booking> found = bookingRepository.findAllByListingId(listing.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByListingVehicleOwnerId_WhenBookingsExist_ShouldReturnMappedBookings() {
        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<Booking> found = bookingRepository.findAllByListingVehicleOwnerId(owner.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Booking::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void findAllByListingVehicleOwnerId_WhenNoBookingsExist_ShouldReturnEmptyList() {
        List<Booking> found = bookingRepository.findAllByListingVehicleOwnerId(owner.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_WhenBookingsExist_ShouldReturnAllMappedBookings() {
        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<Booking> found = bookingRepository.findAll();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
        assertThat(found).extracting(Booking::getId).contains(booking1.getId(), booking2.getId());
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenOverlappingBookingExists_ShouldReturnTrue() {
        BookingEntity existingBooking = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 10), Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 8));

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenNoOverlappingBooking_ShouldReturnFalse() {
        BookingEntity existingBooking = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 10), Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), LocalDate.of(2024, 1, 25), LocalDate.of(2024, 1, 15));

        assertThat(exists).isFalse();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenNoBookingsExist_ShouldReturnFalse() {
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 1));

        assertThat(exists).isFalse();
    }
}
