package com.hassansherwani.medicare.modules.patient.repository;

import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByUserId(Long userId);
}
