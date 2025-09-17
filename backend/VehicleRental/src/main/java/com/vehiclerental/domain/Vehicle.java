package com.vehiclerental.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@Data

public class Vehicle {
    private Integer id;
    private String brand;
    private String model;
    private Date yearOfManufacture;
    private String VIN;
    private String color;
    private Integer ownerId;
}
