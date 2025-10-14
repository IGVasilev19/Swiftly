package com.swiftly.domain;
import com.swiftly.domain.enums.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Profile profile;

    public void attachProfile(Profile p) {
        this.profile = p;
        if (p != null) p.setUser(this);
    }

    public User(String email, String passwordHash, Role role,  Boolean status)
    {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
    }
}
