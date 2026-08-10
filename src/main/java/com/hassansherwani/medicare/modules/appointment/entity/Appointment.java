package com.hassansherwani.medicare.modules.appointment.entity;

import com.hassansherwani.medicare.common.audit.Auditable;
import com.hassansherwani.medicare.modules.appointment.enums.AppointmentStatus;
import com.hassansherwani.medicare.modules.doctor.entity.Doctor;
import com.hassansherwani.medicare.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(nullable = false, length = 500)
    private String reasonForVisit;

    @Column(length = 1000)
    private String doctorNotes; // filled in by DOCTOR after consultation, optional at booking time
}
