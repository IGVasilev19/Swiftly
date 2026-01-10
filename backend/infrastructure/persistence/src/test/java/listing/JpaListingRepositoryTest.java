package listing;

import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ListingEntity;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.listing.JpaListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
class JpaListingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaListingRepository listingRepository;

    private UserEntity ownerUser;
    private ProfileEntity owner;
    private VehicleEntity vehicle;
    private ListingEntity listing;

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

        listing = new ListingEntity(vehicle, "Test Listing", "Description", new BigDecimal("100.00"), true);
        entityManager.persistAndFlush(listing);
    }

    @Test
    void findByVehicleId_WhenListingExists_ShouldReturnListing() {

        ListingEntity found = listingRepository.findByVehicleId(vehicle.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(listing.getId());
        assertThat(found.getVehicle().getId()).isEqualTo(vehicle.getId());
    }

    @Test
    void findByVehicleId_WhenNoListingExists_ShouldReturnNull() {

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

        ListingEntity found = listingRepository.findByVehicleId(vehicle2.getId());

        assertThat(found).isNull();
    }

    @Test
    void findById_WhenListingExists_ShouldReturnListing() {

        Optional<ListingEntity> found = listingRepository.findById(listing.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(listing.getId());
        assertThat(found.get().getTitle()).isEqualTo("Test Listing");
        assertThat(found.get().getVehicle()).isNotNull();
    }

    @Test
    void findById_WhenListingDoesNotExist_ShouldReturnEmpty() {

        Optional<ListingEntity> found = listingRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void findAllWithVehicle_ShouldReturnAllListingsOrderedByCreationDate() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("3HGBH41JXMN109188");
        vehicle2.setMake("BMW");
        vehicle2.setModel("X5");
        vehicle2.setColor("Black");
        vehicle2.setYear(2022);
        vehicle2.setType(VehicleType.SUV);
        vehicle2.setFuelType(FuelType.DIESEL);
        vehicle2.setFuelConsumption(9.0);
        vehicle2.setFeatures(List.of());
        vehicle2.setCountry("Germany");
        vehicle2.setCity("Berlin");
        entityManager.persistAndFlush(vehicle2);

        ListingEntity listing2 = new ListingEntity(vehicle2, "Second Listing", "Description 2", new BigDecimal("150.00"), false);
        entityManager.persistAndFlush(listing2);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        VehicleEntity vehicle3 = new VehicleEntity();
        vehicle3.setOwner(owner);
        vehicle3.setVin("4HGBH41JXMN109189");
        vehicle3.setMake("Tesla");
        vehicle3.setModel("Model 3");
        vehicle3.setColor("Silver");
        vehicle3.setYear(2023);
        vehicle3.setType(VehicleType.CAR);
        vehicle3.setFuelType(FuelType.ELECTRONIC);
        vehicle3.setFuelConsumption(0.0);
        vehicle3.setFeatures(List.of());
        vehicle3.setCountry("USA");
        vehicle3.setCity("Palo Alto");
        entityManager.persistAndFlush(vehicle3);

        ListingEntity listing3 = new ListingEntity(vehicle3, "Third Listing", "Description 3", new BigDecimal("200.00"), true);
        entityManager.persistAndFlush(listing3);

        List<ListingEntity> found = listingRepository.findAllWithVehicle();

        assertThat(found).hasSize(3);

        assertThat(found.get(0).getCreationDate()).isBeforeOrEqualTo(found.get(1).getCreationDate());
        assertThat(found.get(1).getCreationDate()).isBeforeOrEqualTo(found.get(2).getCreationDate());
    }

    @Test
    void findAllWithVehicle_WhenNoListingsExist_ShouldReturnEmptyList() {

        entityManager.remove(listing);
        entityManager.flush();

        List<ListingEntity> found = listingRepository.findAllWithVehicle();

        assertThat(found).isEmpty();
    }

    @Test
    void existsByVehicleId_WhenListingExists_ShouldReturnTrue() {

        Boolean exists = listingRepository.existsByVehicleId(vehicle.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByVehicleId_WhenNoListingExists_ShouldReturnFalse() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("5HGBH41JXMN109190");
        vehicle2.setMake("Ford");
        vehicle2.setModel("Focus");
        vehicle2.setColor("White");
        vehicle2.setYear(2023);
        vehicle2.setType(VehicleType.CAR);
        vehicle2.setFuelType(FuelType.PETROL);
        vehicle2.setFuelConsumption(8.0);
        vehicle2.setFeatures(List.of());
        vehicle2.setCountry("USA");
        vehicle2.setCity("Detroit");
        entityManager.persistAndFlush(vehicle2);

        Boolean exists = listingRepository.existsByVehicleId(vehicle2.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistListing() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("6HGBH41JXMN109191");
        vehicle2.setMake("Audi");
        vehicle2.setModel("A4");
        vehicle2.setColor("Gray");
        vehicle2.setYear(2022);
        vehicle2.setType(VehicleType.CAR);
        vehicle2.setFuelType(FuelType.DIESEL);
        vehicle2.setFuelConsumption(7.0);
        vehicle2.setFeatures(List.of());
        vehicle2.setCountry("Germany");
        vehicle2.setCity("Munich");
        entityManager.persistAndFlush(vehicle2);

        ListingEntity newListing = new ListingEntity(vehicle2, "New Listing", "New Description", new BigDecimal("120.00"), false);

        ListingEntity saved = listingRepository.save(newListing);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(ListingEntity.class, saved.getId())).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("New Listing");
    }

    @Test
    void findAll_ShouldReturnAllListings() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("7HGBH41JXMN109192");
        vehicle2.setMake("Mercedes");
        vehicle2.setModel("C-Class");
        vehicle2.setColor("Black");
        vehicle2.setYear(2023);
        vehicle2.setType(VehicleType.CAR);
        vehicle2.setFuelType(FuelType.PETROL);
        vehicle2.setFuelConsumption(8.0);
        vehicle2.setFeatures(List.of());
        vehicle2.setCountry("Germany");
        vehicle2.setCity("Stuttgart");
        entityManager.persistAndFlush(vehicle2);

        ListingEntity listing2 = new ListingEntity(vehicle2, "Second Listing", "Description 2", new BigDecimal("180.00"), true);
        entityManager.persistAndFlush(listing2);

        List<ListingEntity> all = listingRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(ListingEntity::getId).containsExactlyInAnyOrder(listing.getId(), listing2.getId());
    }
}
