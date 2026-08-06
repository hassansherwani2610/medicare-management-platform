package com.hassansherwani.medicare.modules.patient.serviceImpl;

import com.hassansherwani.medicare.common.exception.DuplicateResourceException;
import com.hassansherwani.medicare.common.exception.ResourceNotFoundException;
import com.hassansherwani.medicare.common.util.FileStorageService;
import com.hassansherwani.medicare.modules.auth.entity.Role;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.auth.enums.RoleName;
import com.hassansherwani.medicare.modules.auth.repository.RoleRepository;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.patient.dto.request.PatientRegisterRequest;
import com.hassansherwani.medicare.modules.patient.dto.response.PatientResponse;
import com.hassansherwani.medicare.modules.patient.entity.Patient;
import com.hassansherwani.medicare.modules.patient.repository.PatientRepository;
import com.hassansherwani.medicare.modules.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Autowired
    public PatientServiceImpl(UserRepository userRepository, RoleRepository roleRepository,PatientRepository patientRepository,PasswordEncoder passwordEncoder,FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    private PatientResponse mapToPatientResponse(User user, Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhoneNumber(patient.getEmergencyContactPhoneNumber())
                .profilePictureUrl(patient.getProfilePictureUrl())
                .build();
    }

    @Override
    public PatientResponse getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for user id: " + userId));

        return mapToPatientResponse(patient.getUser(), patient);
    }

    @Override
    @Transactional
    public PatientResponse registerPatient(PatientRegisterRequest request, MultipartFile profilePicture) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        // Step-1: Upload photo FIRST, fail fast before creating any DB records
        String profilePictureUrl = fileStorageService.uploadImage(profilePicture, "medicare/patients");

        // Step-2: Create the User (identity + PATIENT role)
        Role patientRole = roleRepository.findByName(RoleName.PATIENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("PATIENT role not found, check role seeding"));

        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        // Step-3: Create the Patient profile, linked to that User
        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhoneNumber(request.getEmergencyContactPhoneNumber())
                .profilePictureUrl(profilePictureUrl)
                .build();

        patientRepository.save(patient);

        return mapToPatientResponse(user, patient);
    }
}
