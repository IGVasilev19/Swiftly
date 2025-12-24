package com.swiftly.persistence.entities;
import com.swiftly.domain.Vehicle;
import com.swiftly.domain.VehicleImage;
import com.swiftly.domain.enums.vehicle.Feature;
import com.swiftly.domain.enums.vehicle.FuelType;
import com.swiftly.domain.enums.vehicle.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class VehicleEntity extends Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ProfileEntity owner;

    @Column(nullable = false,  length = 17, unique = true)
    private String vin;

    @Column(nullable = false, length = 100)
    private String make;

    @Column(nullable = false,  length = 100)
    private String model;

    @Column(nullable = false,   length = 100)
    private String color;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private VehicleType type;

    @Column(nullable = false)
    private FuelType fuelType;

    @Column(nullable = false)
    private Double fuelConsumption;

    @ElementCollection(targetClass = Feature.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "vehicle_features",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "feature", nullable = false)
    private List<Feature> features;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(length = 100)
    private Double latitude;

    @Column(length = 100)
    private Double longitude;

    @Column(length = 100)
    private Instant locationTimeStamp;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VehicleImageEntity> images;

    public void addImage(VehicleImageEntity image) {
        images.add(image);
        image.setVehicle(this);
    }

    public void removeImage(VehicleImageEntity image) {
        images.remove(image);
        image.setVehicle(null);
    }

    public void removeImageById(Integer imageId) {
        images.removeIf(img -> img.getId().equals(imageId));
    }

    @Override
    public List<VehicleImage> getImages() {
        List<VehicleImage> images = new ArrayList<>();

        for(VehicleImageEntity vehicleImage : this.images)
        {
            images.add(new VehicleImage(vehicleImage.getId(),vehicleImage.getVehicle(),vehicleImage.getData(), vehicleImage.getMimeType(),vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
        }

        return images;
    }

    @Override
    public void setImages(List<VehicleImage> images) {
        List<VehicleImageEntity> vehicleImages = new ArrayList<>();

        for(VehicleImage vehicleImage : images)
        {
            VehicleEntity vehicleEntity = new VehicleEntity(vehicleImage.getVehicle().getId());

            vehicleImages.add(new VehicleImageEntity(vehicleImage.getId(), vehicleEntity, vehicleImage.getData(), vehicleImage.getMimeType(), vehicleImage.getFileName(), vehicleImage.getUploadedAt()));
        }

        this.images = vehicleImages;
    }


    public VehicleEntity(Integer id)
    {
        this.id = id;
    }

    public VehicleEntity(ProfileEntity owner, String VIN, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption, List<Feature> features, String country, String city)
    {
        this.owner = owner;
        this.vin = VIN;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.features = features;
        this.country = country;
        this.city = city;
    }

    public VehicleEntity(ProfileEntity owner, String VIN, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption, List<Feature> features, String country, String city, List<VehicleImageEntity> images)
    {
        this.owner = owner;
        this.vin = VIN;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.features = features;
        this.country = country;
        this.city = city;
        this.images = images;
    }

    public VehicleEntity(Integer id, ProfileEntity owner, String VIN, String make, String model, String color, Integer year, VehicleType type, FuelType fuelType, Double fuelConsumption, List<Feature> features, String country, String city)
    {
        this.id = id;
        this.owner = owner;
        this.vin = VIN;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
        this.features = features;
        this.country = country;
        this.city = city;
    }
}
