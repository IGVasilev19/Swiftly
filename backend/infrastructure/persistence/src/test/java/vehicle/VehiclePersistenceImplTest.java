package vehicle;

import com.swiftly.application.vehicle.port.outbound.VehicleRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.vehicle.VehiclePersistenceImpl;
import com.swiftly.persistence.helpers.HelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import({VehiclePersistenceImpl.class, HelperImpl.class})
class VehiclePersistenceImplTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity ownerUser;
    private ProfileEntity owner;
    private Profile ownerDomain;

    @BeforeEach
    void setUp() {
        ownerUser = new UserEntity("owner@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(ownerUser);
        owner = new ProfileEntity(ownerUser, "Owner Name", "+1234567890", null);
        entityManager.persistAndFlush(owner);
        ownerDomain = new Profile(owner.getId(), owner.getFullName(), owner.getPhone(), owner.getAvatarUrl());
    }

    @Test
    void save_WhenValidVehicle_ShouldPersistAndReturnMappedVehicle() {
        Vehicle vehicle = new Vehicle(ownerDomain, "1HGBH41JXMN109186", "Toyota", "Camry", "Blue", 2020, VehicleType.CAR, FuelType.PETROL, 8.5, List.of(Feature.AIR_CONDITIONING), "Netherlands", "Amsterdam");

        Vehicle saved = vehicleRepository.save(vehicle);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVin()).isEqualTo("1HGBH41JXMN109186");
        assertThat(saved.getMake()).isEqualTo("Toyota");
        assertThat(saved.getModel()).isEqualTo("Camry");
        assertThat(saved.getColor()).isEqualTo("Blue");
        assertThat(saved.getYear()).isEqualTo(2020);
        assertThat(saved.getType()).isEqualTo(VehicleType.CAR);
        assertThat(saved.getFuelType()).isEqualTo(FuelType.PETROL);
        assertThat(saved.getFuelConsumption()).isEqualTo(8.5);
        assertThat(saved.getFeatures()).contains(Feature.AIR_CONDITIONING);
        assertThat(saved.getCountry()).isEqualTo("Netherlands");
        assertThat(saved.getCity()).isEqualTo("Amsterdam");

        VehicleEntity entity = entityManager.find(VehicleEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getOwner().getId()).isEqualTo(owner.getId());
    }

    @Test
    void save_WhenProfileNotFound_ShouldThrowIllegalArgumentException() {
        Profile nonExistentProfile = new Profile(999);
        Vehicle vehicle = new Vehicle(nonExistentProfile, "1HGBH41JXMN109186", "Toyota", "Camry", "Blue", 2020, VehicleType.CAR, FuelType.PETROL, 8.5, List.of(), "Netherlands", "Amsterdam");

        assertThatThrownBy(() -> vehicleRepository.save(vehicle))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Profile not found");
    }

    @Test
    void findById_WhenVehicleExists_ShouldReturnMappedVehicle() {
        VehicleEntity vehicleEntity = createVehicleEntity();
        entityManager.persistAndFlush(vehicleEntity);

        Vehicle found = vehicleRepository.findById(vehicleEntity.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(vehicleEntity.getId());
        assertThat(found.getVin()).isEqualTo("1HGBH41JXMN109186");
        assertThat(found.getMake()).isEqualTo("Toyota");
        assertThat(found.getOwner()).isNotNull();
        assertThat(found.getOwner().getId()).isEqualTo(owner.getId());
    }

    @Test
    void findById_WhenVehicleDoesNotExist_ShouldThrowException() {
        assertThatThrownBy(() -> vehicleRepository.findById(999))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void findAll_WhenVehiclesExist_ShouldReturnMappedVehicles() {
        VehicleEntity vehicle1 = createVehicleEntity();
        entityManager.persistAndFlush(vehicle1);

        VehicleEntity vehicle2 = createVehicleEntity("2HGBH41JXMN109187", "Honda", "Civic");
        entityManager.persistAndFlush(vehicle2);

        List<Vehicle> found = vehicleRepository.findAll();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
        assertThat(found).extracting(Vehicle::getId).contains(vehicle1.getId(), vehicle2.getId());
    }

    @Test
    void findAllByOwner_WhenVehiclesExist_ShouldReturnMappedVehicles() {
        VehicleEntity vehicle1 = createVehicleEntity();
        entityManager.persistAndFlush(vehicle1);

        VehicleEntity vehicle2 = createVehicleEntity("2HGBH41JXMN109187", "Honda", "Civic");
        entityManager.persistAndFlush(vehicle2);

        UserEntity otherOwnerUser = new UserEntity("other@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(otherOwnerUser);
        ProfileEntity otherOwner = new ProfileEntity(otherOwnerUser, "Other Owner", "+9999999999", null);
        entityManager.persistAndFlush(otherOwner);

        VehicleEntity otherVehicle = createVehicleEntityForOwner(otherOwner, "3HGBH41JXMN109188", "BMW", "X5");
        entityManager.persistAndFlush(otherVehicle);

        List<Vehicle> found = vehicleRepository.findAllByOwner(ownerDomain);

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Vehicle::getId).containsExactlyInAnyOrder(vehicle1.getId(), vehicle2.getId());
        assertThat(found).extracting(Vehicle::getOwner).extracting(Profile::getId).containsOnly(owner.getId());
    }

    @Test
    void findAllByOwner_WhenNoVehiclesExist_ShouldReturnEmptyList() {
        Profile otherOwner = new Profile(999);
        List<Vehicle> found = vehicleRepository.findAllByOwner(otherOwner);

        assertThat(found).isEmpty();
    }

    @Test
    void findByVin_WhenVinExists_ShouldReturnMappedVehicle() {
        VehicleEntity vehicleEntity = createVehicleEntity();
        entityManager.persistAndFlush(vehicleEntity);

        Vehicle found = vehicleRepository.findByVin("1HGBH41JXMN109186");

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(vehicleEntity.getId());
        assertThat(found.getVin()).isEqualTo("1HGBH41JXMN109186");
    }

    @Test
    void findByVin_WhenVinDoesNotExist_ShouldThrowNullPointerException() {
        assertThatThrownBy(() -> vehicleRepository.findByVin("NONEXISTENTVIN"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void existsByVin_WhenVinExists_ShouldReturnTrue() {
        VehicleEntity vehicleEntity = createVehicleEntity();
        entityManager.persistAndFlush(vehicleEntity);

        Boolean exists = vehicleRepository.existsByVin("1HGBH41JXMN109186");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByVin_WhenVinDoesNotExist_ShouldReturnFalse() {
        Boolean exists = vehicleRepository.existsByVin("NONEXISTENTVIN");

        assertThat(exists).isFalse();
    }

    @Test
    void deleteById_ShouldDeleteVehicle() {
        VehicleEntity vehicleEntity = createVehicleEntity();
        entityManager.persistAndFlush(vehicleEntity);
        Integer vehicleId = vehicleEntity.getId();

        vehicleRepository.deleteById(vehicleId);
        entityManager.flush();

        assertThat(entityManager.find(VehicleEntity.class, vehicleId)).isNull();
    }

    private VehicleEntity createVehicleEntity() {
        return createVehicleEntity("1HGBH41JXMN109186", "Toyota", "Camry");
    }

    private VehicleEntity createVehicleEntity(String vin, String make, String model) {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setOwner(owner);
        vehicleEntity.setVin(vin);
        vehicleEntity.setMake(make);
        vehicleEntity.setModel(model);
        vehicleEntity.setColor("Blue");
        vehicleEntity.setYear(2020);
        vehicleEntity.setType(VehicleType.CAR);
        vehicleEntity.setFuelType(FuelType.PETROL);
        vehicleEntity.setFuelConsumption(8.5);
        vehicleEntity.setFeatures(List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH));
        vehicleEntity.setCountry("Netherlands");
        vehicleEntity.setCity("Amsterdam");
        return vehicleEntity;
    }

    private VehicleEntity createVehicleEntityForOwner(ProfileEntity ownerEntity, String vin, String make, String model) {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setOwner(ownerEntity);
        vehicleEntity.setVin(vin);
        vehicleEntity.setMake(make);
        vehicleEntity.setModel(model);
        vehicleEntity.setColor("Red");
        vehicleEntity.setYear(2021);
        vehicleEntity.setType(VehicleType.SUV);
        vehicleEntity.setFuelType(FuelType.DIESEL);
        vehicleEntity.setFuelConsumption(9.0);
        vehicleEntity.setFeatures(List.of());
        vehicleEntity.setCountry("Germany");
        vehicleEntity.setCity("Berlin");
        return vehicleEntity;
    }
}
