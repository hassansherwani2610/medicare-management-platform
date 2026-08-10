package com.hassansherwani.medicare.modules.appointment.dto.response;

import com.hassansherwani.medicare.modules.appointment.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String reasonForVisit;
    private String doctorNotes;
}