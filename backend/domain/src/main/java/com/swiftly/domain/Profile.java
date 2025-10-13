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
}
