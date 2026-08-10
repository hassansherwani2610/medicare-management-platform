package com.hassansherwani.medicare.modules.appointment.service;

import com.hassansherwani.medicare.modules.appointment.dto.request.AppointmentBookRequest;
import com.hassansherwani.medicare.modules.appointment.dto.response.AppointmentResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentService {
    AppointmentResponse bookAppointment(String patientEmail, AppointmentBookRequest request);
    List<AppointmentResponse> getMyAppointmentsAsPatient(String patientEmail);
    List<AppointmentResponse> getMyAppointmentsAsDoctor(String doctorEmail);
    AppointmentResponse cancelAppointment(String requesterEmail, Long appointmentId);
    AppointmentResponse confirmAppointment(String doctorEmail, Long appointmentId);
}