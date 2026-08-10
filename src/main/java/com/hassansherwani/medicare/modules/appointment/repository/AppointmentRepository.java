package com.hassansherwani.medicare.modules.appointment.repository;

import com.hassansherwani.medicare.modules.appointment.entity.Appointment;
import com.hassansherwani.medicare.modules.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusIn(
            Long doctorId,
            LocalDateTime appointmentDateTime,
            List<AppointmentStatus> statuses
    );
}
