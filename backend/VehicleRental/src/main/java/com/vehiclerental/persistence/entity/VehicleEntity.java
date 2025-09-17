package com.vehiclerental.persistence.entity;

import lombok.*;

import java.sql.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class VehicleEntity {
    private Integer id;
    private String brand;
    private String model;
    private Date yearOfManufacture;
    private String VIN;
    private String color;
    private Integer ownerId;
}
