package booking;

import com.swiftly.domain.enums.booking.Status;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.booking.JpaBookingRepository;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.BookingEntity;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
class JpaBookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaBookingRepository bookingRepository;

    private UserEntity ownerUser;
    private UserEntity renterUser;
    private ProfileEntity owner;
    private ProfileEntity renter;
    private VehicleEntity vehicle;
    private ListingEntity listing;

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
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenOverlappingBookingExists_ShouldReturnTrue() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 10);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 8);
        LocalDate checkEnd = LocalDate.of(2024, 1, 12);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenNoOverlappingBooking_ShouldReturnFalse() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 10);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 15);
        LocalDate checkEnd = LocalDate.of(2024, 1, 20);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isFalse();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingStartsBeforeAndEndsDuring_ShouldReturnTrue() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 15);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("1100.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 1);
        LocalDate checkEnd = LocalDate.of(2024, 1, 10);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingStartsDuringAndEndsAfter_ShouldReturnTrue() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 15);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("1100.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 10);
        LocalDate checkEnd = LocalDate.of(2024, 1, 20);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingCompletelyContains_ShouldReturnTrue() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 15);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("1100.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 7);
        LocalDate checkEnd = LocalDate.of(2024, 1, 12);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingCompletelySurrounds_ShouldReturnTrue() {

        LocalDate existingStart = LocalDate.of(2024, 1, 7);
        LocalDate existingEnd = LocalDate.of(2024, 1, 12);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 5);
        LocalDate checkEnd = LocalDate.of(2024, 1, 15);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingStartsExactlyAtEnd_ShouldReturnTrueForAdjacentDates() {

        LocalDate existingStart = LocalDate.of(2024, 1, 5);
        LocalDate existingEnd = LocalDate.of(2024, 1, 10);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 10);
        LocalDate checkEnd = LocalDate.of(2024, 1, 15);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenBookingEndsExactlyAtStart_ShouldReturnTrueForAdjacentDates() {

        LocalDate existingStart = LocalDate.of(2024, 1, 10);
        LocalDate existingEnd = LocalDate.of(2024, 1, 15);
        BookingEntity existingBooking = new BookingEntity(listing, renter, existingStart, existingEnd, Status.REQUESTED, new BigDecimal("600.00"));
        entityManager.persistAndFlush(existingBooking);

        LocalDate checkStart = LocalDate.of(2024, 1, 5);
        LocalDate checkEnd = LocalDate.of(2024, 1, 10);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenNoBookingsExist_ShouldReturnFalse() {

        LocalDate checkStart = LocalDate.of(2024, 1, 1);
        LocalDate checkEnd = LocalDate.of(2024, 1, 10);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isFalse();
    }

    @Test
    void existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual_WhenDifferentListing_ShouldReturnFalse() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("2HGBH41JXMN109187");
        vehicle2.setMake("Honda");
        vehicle2.setModel("Civic");
        vehicle2.setColor("Red");
        vehicle2.setYear(2021);
        vehicle2.setType(VehicleType.CAR);
        vehicle2.setFuelType(FuelType.PETROL);
        vehicle2.setFuelConsumption(7.5);
        vehicle2.setFeatures(List.of());
        vehicle2.setCountry("Netherlands");
        vehicle2.setCity("Rotterdam");
        entityManager.persistAndFlush(vehicle2);

        ListingEntity listing2 = new ListingEntity(vehicle2, "Second Listing", "Description", new BigDecimal("150.00"), false);
        entityManager.persistAndFlush(listing2);

        BookingEntity booking = new BookingEntity(listing2, renter, LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 10), Status.REQUESTED, new BigDecimal("750.00"));
        entityManager.persistAndFlush(booking);

        LocalDate checkStart = LocalDate.of(2024, 1, 5);
        LocalDate checkEnd = LocalDate.of(2024, 1, 10);
        Boolean exists = bookingRepository.existsByListingIdAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                listing.getId(), checkEnd, checkStart);

        assertThat(exists).isFalse();
    }

    @Test
    void findById_WhenBookingExists_ShouldReturnBookingWithFetchedRelations() {

        BookingEntity booking = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking);

        Optional<BookingEntity> found = bookingRepository.findById(booking.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(booking.getId());
        assertThat(found.get().getListing()).isNotNull();
        assertThat(found.get().getRenter()).isNotNull();
    }

    @Test
    void findById_WhenBookingDoesNotExist_ShouldReturnEmpty() {

        Optional<BookingEntity> found = bookingRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByRenterId_WhenBookingsExist_ShouldReturnAllBookings() {

        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<BookingEntity> found = bookingRepository.findAllByRenterId(renter.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(BookingEntity::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void findAllByRenterId_WhenNoBookingsExist_ShouldReturnEmptyList() {

        List<BookingEntity> found = bookingRepository.findAllByRenterId(renter.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByListingId_WhenBookingsExist_ShouldReturnAllBookingsOrderedByCreationDate() {

        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<BookingEntity> found = bookingRepository.findAllByListingId(listing.getId());

        assertThat(found).hasSize(2);
        assertThat(found.get(0).getCreationDate()).isBeforeOrEqualTo(found.get(1).getCreationDate());
    }

    @Test
    void findAllByListingId_WhenNoBookingsExist_ShouldReturnEmptyList() {

        List<BookingEntity> found = bookingRepository.findAllByListingId(listing.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByListingVehicleOwnerId_WhenBookingsExist_ShouldReturnAllBookingsForOwner() {

        BookingEntity booking1 = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking1);

        BookingEntity booking2 = new BookingEntity(listing, renter, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 5), Status.APPROVED, new BigDecimal("500.00"));
        entityManager.persistAndFlush(booking2);

        List<BookingEntity> found = bookingRepository.findAllByListingVehicleOwnerId(owner.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(BookingEntity::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void findAllByListingVehicleOwnerId_WhenNoBookingsExist_ShouldReturnEmptyList() {

        List<BookingEntity> found = bookingRepository.findAllByListingVehicleOwnerId(owner.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistBooking() {

        BookingEntity booking = new BookingEntity(listing, renter, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), Status.REQUESTED, new BigDecimal("500.00"));

        BookingEntity saved = bookingRepository.save(booking);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(BookingEntity.class, saved.getId())).isNotNull();
    }
}
