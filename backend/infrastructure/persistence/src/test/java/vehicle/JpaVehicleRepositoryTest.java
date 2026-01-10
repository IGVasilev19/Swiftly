package vehicle;

import com.swiftly.domain.enums.user.Role;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.entities.VehicleEntity;
import com.swiftly.persistence.vehicle.JpaVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
class JpaVehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaVehicleRepository vehicleRepository;

    private UserEntity ownerUser;
    private ProfileEntity owner;
    private VehicleEntity vehicle;

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
        vehicle.setFeatures(List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH));
        vehicle.setCountry("Netherlands");
        vehicle.setCity("Amsterdam");
        entityManager.persistAndFlush(vehicle);
    }

    @Test
    void findAllByOwner_WhenVehiclesExist_ShouldReturnAllVehiclesForOwner() {

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
        vehicle2.setFeatures(List.of(Feature.AIR_CONDITIONING));
        vehicle2.setCountry("Netherlands");
        vehicle2.setCity("Rotterdam");
        entityManager.persistAndFlush(vehicle2);

        List<VehicleEntity> found = vehicleRepository.findAllByOwner(owner.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(VehicleEntity::getId).containsExactlyInAnyOrder(vehicle.getId(), vehicle2.getId());
    }

    @Test
    void findAllByOwner_WhenNoVehiclesExist_ShouldReturnEmptyList() {

        UserEntity otherOwnerUser = new UserEntity("other@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(otherOwnerUser);
        ProfileEntity otherOwner = new ProfileEntity(otherOwnerUser, "Other Owner", "+9999999999", null);
        entityManager.persistAndFlush(otherOwner);

        List<VehicleEntity> found = vehicleRepository.findAllByOwner(otherOwner.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByOwner_WhenDifferentOwner_ShouldNotReturnVehicles() {

        UserEntity otherOwnerUser = new UserEntity("other@test.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(otherOwnerUser);
        ProfileEntity otherOwner = new ProfileEntity(otherOwnerUser, "Other Owner", "+9999999999", null);
        entityManager.persistAndFlush(otherOwner);

        VehicleEntity otherVehicle = new VehicleEntity();
        otherVehicle.setOwner(otherOwner);
        otherVehicle.setVin("3HGBH41JXMN109188");
        otherVehicle.setMake("BMW");
        otherVehicle.setModel("X5");
        otherVehicle.setColor("Black");
        otherVehicle.setYear(2022);
        otherVehicle.setType(VehicleType.SUV);
        otherVehicle.setFuelType(FuelType.DIESEL);
        otherVehicle.setFuelConsumption(9.0);
        otherVehicle.setFeatures(List.of());
        otherVehicle.setCountry("Germany");
        otherVehicle.setCity("Berlin");
        entityManager.persistAndFlush(otherVehicle);

        List<VehicleEntity> found = vehicleRepository.findAllByOwner(owner.getId());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(vehicle.getId());
    }

    @Test
    void findByVin_WhenVinExists_ShouldReturnVehicle() {

        VehicleEntity found = vehicleRepository.findByVin("1HGBH41JXMN109186");

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(vehicle.getId());
        assertThat(found.getVin()).isEqualTo("1HGBH41JXMN109186");
    }

    @Test
    void findByVin_WhenVinDoesNotExist_ShouldReturnNull() {

        VehicleEntity found = vehicleRepository.findByVin("NONEXISTENTVIN");

        assertThat(found).isNull();
    }

    @Test
    void findById_WhenVehicleExists_ShouldReturnVehicle() {

        Optional<VehicleEntity> found = vehicleRepository.findById(vehicle.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(vehicle.getId());
        assertThat(found.get().getVin()).isEqualTo("1HGBH41JXMN109186");
    }

    @Test
    void findById_WhenVehicleDoesNotExist_ShouldReturnEmpty() {

        Optional<VehicleEntity> found = vehicleRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void existsByVin_WhenVinExists_ShouldReturnTrue() {

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

        Integer vehicleId = vehicle.getId();

        vehicleRepository.deleteById(vehicleId);
        entityManager.flush();

        assertThat(entityManager.find(VehicleEntity.class, vehicleId)).isNull();
    }

    @Test
    void save_ShouldPersistVehicle() {

        VehicleEntity newVehicle = new VehicleEntity();
        newVehicle.setOwner(owner);
        newVehicle.setVin("4HGBH41JXMN109189");
        newVehicle.setMake("Ford");
        newVehicle.setModel("Focus");
        newVehicle.setColor("White");
        newVehicle.setYear(2023);
        newVehicle.setType(VehicleType.CAR);
        newVehicle.setFuelType(FuelType.ELECTRONIC);
        newVehicle.setFuelConsumption(0.0);
        newVehicle.setFeatures(List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH, Feature.NAVIGATION));
        newVehicle.setCountry("USA");
        newVehicle.setCity("Detroit");

        VehicleEntity saved = vehicleRepository.save(newVehicle);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(VehicleEntity.class, saved.getId())).isNotNull();
        assertThat(saved.getVin()).isEqualTo("4HGBH41JXMN109189");
    }

    @Test
    void findAll_ShouldReturnAllVehicles() {

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setOwner(owner);
        vehicle2.setVin("5HGBH41JXMN109190");
        vehicle2.setMake("Tesla");
        vehicle2.setModel("Model 3");
        vehicle2.setColor("Silver");
        vehicle2.setYear(2023);
        vehicle2.setType(VehicleType.CAR);
        vehicle2.setFuelType(FuelType.ELECTRONIC);
        vehicle2.setFuelConsumption(0.0);
        vehicle2.setFeatures(List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH, Feature.NAVIGATION));
        vehicle2.setCountry("USA");
        vehicle2.setCity("Palo Alto");
        entityManager.persistAndFlush(vehicle2);

        List<VehicleEntity> all = vehicleRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(VehicleEntity::getId).containsExactlyInAnyOrder(vehicle.getId(), vehicle2.getId());
    }
}
