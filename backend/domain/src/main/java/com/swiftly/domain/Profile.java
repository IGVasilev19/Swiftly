package com.swiftly.domain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Profile {

    @Id
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

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

    public Profile(User user, String fullName, String phone, String address, String City, String Country, String postalCode)
    {
        this.user = user;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.City = City;
        this.Country = Country;
        this.postalCode = postalCode;
    }

    public Profile(Integer id)
    {
        this.id = id;
        this.user = null;
        this.fullName = null;
        this.phone = null;
        this.address = null;
        this.City = null;
        this.Country = null;
        this.postalCode = null;
    }
}
