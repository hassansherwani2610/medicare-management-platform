package com.hassansherwani.medicare.modules.auth.entity;

import com.hassansherwani.medicare.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hash — never plain text

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true; // for account activation/deactivation

    @Column(nullable = false)
    @Builder.Default
    private Boolean accountLocked = false; // for security lockout after failed attempts

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
