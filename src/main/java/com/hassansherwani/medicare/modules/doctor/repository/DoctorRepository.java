package com.hassansherwani.medicare.modules.doctor.repository;

import com.hassansherwani.medicare.modules.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    boolean existsByLicenseNumber(String licenseNumber);
}
