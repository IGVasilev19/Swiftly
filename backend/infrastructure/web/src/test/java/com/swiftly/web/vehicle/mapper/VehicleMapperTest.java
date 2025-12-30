package com.swiftly.web.vehicle.mapper;

import com.swiftly.domain.Profile;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import com.swiftly.web.vehicle.dto.VehicleRequest;
import com.swiftly.web.vehicle.dto.VehicleResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleMapperTest {

    @Test
    void toVehicle_WithValidRequest_ShouldMapCorrectly() {
        String vin = "1HGBH41JXMN12345";
        String make = "Toyota";
        String model = "Camry";
        String color = "Blue";
        Integer year = 2020;
        VehicleType type = VehicleType.CAR;
        FuelType fuelType = FuelType.PETROL;
        Double fuelConsumption = 8.5;
        List<Feature> features = List.of(Feature.AIR_CONDITIONING, Feature.BLUETOOTH);
        String country = "Netherlands";
        String city = "Amsterdam";

        VehicleRequest request = new VehicleRequest(vin, make, model, color, year, type, fuelType, fuelConsumption, features, country, city);

        Vehicle vehicle = VehicleMapper.toVehicle(request);

        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getVin()).isEqualTo(vin);
        assertThat(vehicle.getMake()).isEqualTo(make);
        assertThat(vehicle.getModel()).isEqualTo(model);
        assertThat(vehicle.getColor()).isEqualTo(color);
        assertThat(vehicle.getYear()).isEqualTo(year);
        assertThat(vehicle.getType()).isEqualTo(type);
        assertThat(vehicle.getFuelType()).isEqualTo(fuelType);
        assertThat(vehicle.getFuelConsumption()).isEqualTo(fuelConsumption);
        assertThat(vehicle.getFeatures()).isEqualTo(features);
        assertThat(vehicle.getCountry()).isEqualTo(country);
        assertThat(vehicle.getCity()).isEqualTo(city);
    }

    @Test
    void toVehicleResponse_WithValidVehicle_ShouldMapCorrectly() {
        Profile owner = new Profile(1, "John Doe", "+1234567890", null);
        Vehicle vehicle = new Vehicle(1, owner, "1HGBH41JXMN12345", "Toyota", "Camry", "Blue", 2020, VehicleType.CAR, FuelType.PETROL, 8.5, List.of(Feature.AIR_CONDITIONING), "Netherlands", "Amsterdam", List.of());

        VehicleResponse response = VehicleMapper.toVehicleResponse(vehicle);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.vin()).isEqualTo("1HGBH41JXMN12345");
        assertThat(response.make()).isEqualTo("Toyota");
        assertThat(response.model()).isEqualTo("Camry");
        assertThat(response.color()).isEqualTo("Blue");
        assertThat(response.year()).isEqualTo(2020);
        assertThat(response.type()).isEqualTo(VehicleType.CAR);
        assertThat(response.fuelType()).isEqualTo(FuelType.PETROL);
        assertThat(response.fuelConsumption()).isEqualTo(8.5);
        assertThat(response.features()).isEqualTo(List.of(Feature.AIR_CONDITIONING));
        assertThat(response.country()).isEqualTo("Netherlands");
        assertThat(response.city()).isEqualTo("Amsterdam");
        assertThat(response.listed()).isFalse();
        assertThat(response.owner()).isNotNull();
    }

    @Test
    void toVehicleResponse_WithNullImages_ShouldMapCorrectly() {
        Profile owner = new Profile(1, "John Doe", "+1234567890", null);
        Vehicle vehicle = new Vehicle(1, owner, "1HGBH41JXMN12345", "Toyota", "Camry", "Blue", 2020, VehicleType.CAR, FuelType.PETROL, 8.5, List.of(), "Netherlands", "Amsterdam", null);

        VehicleResponse response = VehicleMapper.toVehicleResponse(vehicle);

        assertThat(response).isNotNull();
        assertThat(response.images()).isNull();
    }
}

