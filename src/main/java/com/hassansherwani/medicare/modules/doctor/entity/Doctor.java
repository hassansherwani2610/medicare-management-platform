package com.hassansherwani.medicare.modules.doctor.entity;

import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.doctor.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(nullable = false)
    private Integer yearsOfExperience;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 500)
    private String qualifications; // e.g. "MBBS, FCPS, e.t.c"

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true; // is this doctor currently accepting appointments

    @Column(nullable = false, length = 1000)
    private String profilePictureUrl; // Cloudinary-hosted URL
}
