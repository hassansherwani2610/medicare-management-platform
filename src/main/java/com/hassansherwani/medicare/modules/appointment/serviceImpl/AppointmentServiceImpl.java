package com.hassansherwani.medicare.modules.appointment.serviceImpl;

import com.hassansherwani.medicare.common.exception.BusinessRuleViolationException;
import com.hassansherwani.medicare.common.exception.ResourceNotFoundException;
import com.hassansherwani.medicare.modules.appointment.dto.request.AppointmentBookRequest;
import com.hassansherwani.medicare.modules.appointment.dto.response.AppointmentResponse;
import com.hassansherwani.medicare.modules.appointment.entity.Appointment;
import com.hassansherwani.medicare.modules.appointment.enums.AppointmentStatus;
import com.hassansherwani.medicare.modules.appointment.repository.AppointmentRepository;
import com.hassansherwani.medicare.modules.appointment.service.AppointmentService;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.doctor.entity.Doctor;
import com.hassansherwani.medicare.modules.doctor.repository.DoctorRepository;
import com.hassansherwani.medicare.modules.patient.entity.Patient;
import com.hassansherwani.medicare.modules.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private static final List<AppointmentStatus> ACTIVE_STATUSES =
            List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED);

    @Autowired
    public AppointmentServiceImpl(UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getUser().getFullName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getUser().getFullName())
                .doctorSpecialization(appointment.getDoctor().getSpecialization().name())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .reasonForVisit(appointment.getReasonForVisit())
                .doctorNotes(appointment.getDoctorNotes())
                .build();
    }

    private Patient getPatientByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for this account"));
    }

    private Doctor getDoctorByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for this account"));
    }

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(String patientEmail, AppointmentBookRequest request) {
        Patient patient = getPatientByEmail(patientEmail);

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        if (!doctor.isAvailable()) {
            throw new BusinessRuleViolationException("This doctor is not currently accepting appointments");
        }

        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(
                doctor.getId(), request.getAppointmentDateTime(), ACTIVE_STATUSES
        );
        if (slotTaken) {
            throw new BusinessRuleViolationException("This time slot is already booked for the selected doctor");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.getAppointmentDateTime())
                .reasonForVisit(request.getReasonForVisit())
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);

        return mapToAppointmentResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsAsPatient(String patientEmail) {
        Patient patient = getPatientByEmail(patientEmail);
        return appointmentRepository.findByPatientId(patient.getId())
                .stream().map(this::mapToAppointmentResponse).toList();
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsAsDoctor(String doctorEmail) {
        Doctor doctor = getDoctorByEmail(doctorEmail);
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream().map(this::mapToAppointmentResponse).toList();
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(String requesterEmail, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        boolean isOwnerPatient = appointment.getPatient().getUser().getEmail().equalsIgnoreCase(requesterEmail);
        boolean isOwnerDoctor = appointment.getDoctor().getUser().getEmail().equalsIgnoreCase(requesterEmail);

        if (!isOwnerPatient && !isOwnerDoctor) {
            throw new BusinessRuleViolationException("You are not authorized to cancel this appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BusinessRuleViolationException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);

        return mapToAppointmentResponse(appointment);
    }


    @Override
    @Transactional
    public AppointmentResponse confirmAppointment(String doctorEmail, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getDoctor().getUser().getEmail().equalsIgnoreCase(doctorEmail)) {
            throw new BusinessRuleViolationException("You are not authorized to confirm this appointment");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessRuleViolationException("Only pending appointments can be confirmed");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);

        appointmentRepository.save(appointment);

        return mapToAppointmentResponse(appointment);
    }
}
