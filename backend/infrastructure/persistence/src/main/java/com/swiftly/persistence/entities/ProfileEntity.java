package com.swiftly.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileEntity {

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

    @Column
    private String avatarUrl;

    public ProfileEntity(Integer id)
    {
        this.id = id;
    }

    public ProfileEntity(UserEntity userEntity, String fullName, String phone, String avatarUrl) {
        this.user = userEntity;
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }
}
