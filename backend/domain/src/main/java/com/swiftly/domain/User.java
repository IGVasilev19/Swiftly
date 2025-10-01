package com.swiftly.domain;
import com.swiftly.domain.enums.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true ,length = 150)
    private String email;
    @Column(nullable = false, length = 255)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean status;
}
