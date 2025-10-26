package com.swiftly.persistence.entities;

import com.swiftly.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileEntity extends Profile {

    @Id
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 17)
    private String phone;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 100)
    private String City;

    @Column(nullable = false, length = 100)
    private String Country;

    @Column(nullable = false, length = 100)
    private String postalCode;

    private String avatarUrl;
    private String locale;
    private String timezone;

    public ProfileEntity(UserEntity userEntity, String fullName, String phone, String address, String city, String country, String postalCode) {
        this.user = userEntity;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.City = city;
        this.Country = country;
        this.postalCode = postalCode;
    }
}
