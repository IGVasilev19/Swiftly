package listing;

import com.swiftly.application.listing.outbound.ListingRepository;
import com.swiftly.domain.Listing;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.listing.ListingPersistenceImpl;
import com.swiftly.persistence.helpers.HelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import({ListingPersistenceImpl.class, HelperImpl.class})
class ListingPersistenceImplTest {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity ownerUser;
    private ProfileEntity owner;
    private VehicleEntity vehicle;
    private Vehicle vehicleDomain;

    @BeforeEach
    void setUp() {
        ownerUser = new UserEntity("owner@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(ownerUser);
        owner = new ProfileEntity(ownerUser, "Owner Name", "+1234567890", null);
        entityManager.persistAndFlush(owner);

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

        vehicleDomain = new Vehicle(vehicle.getId());
    }

    @Test
    void save_WhenValidListing_ShouldPersistAndReturnMappedListing() {
        Listing listing = new Listing(vehicleDomain, "Test Listing", "Description", new BigDecimal("100.00"), true);

        Listing saved = listingRepository.save(listing);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Listing");
        assertThat(saved.getDescription()).isEqualTo("Description");
        assertThat(saved.getBasePricePerDay()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(saved.getInstantBook()).isTrue();
        assertThat(saved.getVehicle()).isNotNull();
        assertThat(saved.getVehicle().getId()).isEqualTo(vehicle.getId());

        ListingEntity entity = entityManager.find(ListingEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getVehicle().getId()).isEqualTo(vehicle.getId());
    }

    @Test
    void save_WhenVehicleNotFound_ShouldThrowIllegalArgumentException() {
        Vehicle nonExistentVehicle = new Vehicle(999);
        Listing listing = new Listing(nonExistentVehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);

        assertThatThrownBy(() -> listingRepository.save(listing))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vehicle not found");
    }

    @Test
    void findById_WhenListingExists_ShouldReturnMappedListing() {
        ListingEntity listingEntity = new ListingEntity(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listingEntity);

        Listing found = listingRepository.findById(listingEntity.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(listingEntity.getId());
        assertThat(found.getTitle()).isEqualTo("Test Listing");
        assertThat(found.getDescription()).isEqualTo("Description");
        assertThat(found.getBasePricePerDay()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(found.getInstantBook()).isTrue();
    }

    @Test
    void findById_WhenListingDoesNotExist_ShouldThrowException() {
        assertThatThrownBy(() -> listingRepository.findById(999))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void findByVehicleId_WhenListingExists_ShouldReturnMappedListing() {
        ListingEntity listingEntity = new ListingEntity(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listingEntity);

        Listing found = listingRepository.findByVehicleId(vehicle.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(listingEntity.getId());
        assertThat(found.getVehicle().getId()).isEqualTo(vehicle.getId());
    }

    @Test
    void findByVehicleId_WhenNoListingExists_ShouldThrowNullPointerException() {
        assertThatThrownBy(() -> listingRepository.findByVehicleId(vehicle.getId()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void existsByVehicleId_WhenListingExists_ShouldReturnTrue() {
        ListingEntity listingEntity = new ListingEntity(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listingEntity);

        Boolean exists = listingRepository.existsByVehicleId(vehicle.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByVehicleId_WhenNoListingExists_ShouldReturnFalse() {
        Boolean exists = listingRepository.existsByVehicleId(vehicle.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void findAll_WhenListingsExist_ShouldReturnMappedListings() {
        ListingEntity listing1 = new ListingEntity(vehicle, "Listing 1", "Description 1", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listing1);

        VehicleEntity vehicle2 = createVehicleEntity("2HGBH41JXMN109187", "Honda", "Civic");
        entityManager.persistAndFlush(vehicle2);
        ListingEntity listing2 = new ListingEntity(vehicle2, "Listing 2", "Description 2", new BigDecimal("150.00"), false);
        entityManager.persistAndFlush(listing2);

        List<Listing> found = listingRepository.findAll();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
        assertThat(found).extracting(Listing::getId).contains(listing1.getId(), listing2.getId());
    }

    @Test
    void findAllWithVehicle_WhenListingsExist_ShouldReturnMappedListings() {
        ListingEntity listing1 = new ListingEntity(vehicle, "Listing 1", "Description 1", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listing1);

        VehicleEntity vehicle2 = createVehicleEntity("2HGBH41JXMN109187", "Honda", "Civic");
        entityManager.persistAndFlush(vehicle2);
        ListingEntity listing2 = new ListingEntity(vehicle2, "Listing 2", "Description 2", new BigDecimal("150.00"), false);
        entityManager.persistAndFlush(listing2);

        List<Listing> found = listingRepository.findAllWithVehicle();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
        assertThat(found).extracting(Listing::getId).contains(listing1.getId(), listing2.getId());
        assertThat(found).allMatch(listing -> listing.getVehicle() != null);
    }

    private VehicleEntity createVehicleEntity(String vin, String make, String model) {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setOwner(owner);
        vehicleEntity.setVin(vin);
        vehicleEntity.setMake(make);
        vehicleEntity.setModel(model);
        vehicleEntity.setColor("Red");
        vehicleEntity.setYear(2021);
        vehicleEntity.setType(VehicleType.CAR);
        vehicleEntity.setFuelType(FuelType.PETROL);
        vehicleEntity.setFuelConsumption(7.5);
        vehicleEntity.setFeatures(List.of());
        vehicleEntity.setCountry("Netherlands");
        vehicleEntity.setCity("Rotterdam");
        return vehicleEntity;
    }
}
