package com.hassansherwani.medicare.modules.appointment.controller;

import com.hassansherwani.medicare.common.response.ApiResponse;
import com.hassansherwani.medicare.modules.appointment.dto.request.AppointmentBookRequest;
import com.hassansherwani.medicare.modules.appointment.dto.response.AppointmentResponse;
import com.hassansherwani.medicare.modules.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(@Valid @RequestBody AppointmentBookRequest request, Authentication authentication) {
        AppointmentResponse response = appointmentService.bookAppointment(authentication.getName(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully", response));
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointmentsAsPatient(Authentication authentication) {
        List<AppointmentResponse> response = appointmentService.getMyAppointmentsAsPatient(authentication.getName());

        return ResponseEntity.ok(ApiResponse.success("Appointments fetched successfully", response));
    }

    @GetMapping("/my-schedule")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointmentsAsDoctor(Authentication authentication) {
        List<AppointmentResponse> response = appointmentService.getMyAppointmentsAsDoctor(authentication.getName());

        return ResponseEntity.ok(ApiResponse.success("Schedule fetched successfully", response));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable Long id, Authentication authentication) {
        AppointmentResponse response = appointmentService.cancelAppointment(authentication.getName(), id);

        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", response));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(@PathVariable Long id, Authentication authentication) {
        AppointmentResponse response = appointmentService.confirmAppointment(authentication.getName(), id);

        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed successfully", response));
    }
}