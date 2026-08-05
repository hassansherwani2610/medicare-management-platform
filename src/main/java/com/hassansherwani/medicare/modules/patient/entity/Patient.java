package com.hassansherwani.medicare.modules.patient.entity;

import com.hassansherwani.medicare.common.audit.Auditable;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.patient.enums.BloodGroup;
import com.hassansherwani.medicare.modules.patient.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String emergencyContactName;

    @Column(nullable = false, length = 20)
    private String emergencyContactPhone;

    @Column(nullable = false)
    private String profilePictureUrl; // Cloudinary-hosted URL
}
