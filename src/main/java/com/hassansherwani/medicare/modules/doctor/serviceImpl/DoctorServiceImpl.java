package com.hassansherwani.medicare.modules.doctor.serviceImpl;

import com.hassansherwani.medicare.common.exception.BusinessRuleViolationException;
import com.hassansherwani.medicare.common.exception.DuplicateResourceException;
import com.hassansherwani.medicare.common.exception.ResourceNotFoundException;
import com.hassansherwani.medicare.common.util.FileStorageService;
import com.hassansherwani.medicare.modules.auth.entity.Role;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.auth.enums.RoleName;
import com.hassansherwani.medicare.modules.auth.repository.RoleRepository;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.doctor.dto.request.DoctorRegisterRequest;
import com.hassansherwani.medicare.modules.doctor.dto.response.DoctorResponse;
import com.hassansherwani.medicare.modules.doctor.entity.Doctor;
import com.hassansherwani.medicare.modules.doctor.repository.DoctorRepository;
import com.hassansherwani.medicare.modules.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Autowired
    public DoctorServiceImpl(UserRepository userRepository, RoleRepository roleRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    private DoctorResponse mapToDoctorResponse(User user, Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .consultationFee(doctor.getConsultationFee())
                .phoneNumber(doctor.getPhoneNumber())
                .qualifications(doctor.getQualifications())
                .available(doctor.isAvailable())
                .profilePictureUrl(doctor.getProfilePictureUrl())
                .build();
    }

    @Override
    public DoctorResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for this account"));

        return mapToDoctorResponse(user, doctor);
    }

    @Override
    @Transactional
    public DoctorResponse registerDoctor(DoctorRegisterRequest request, MultipartFile profilePicture) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BusinessRuleViolationException("License number already registered: " + request.getLicenseNumber());
        }

        // Step-1: Upload photo FIRST, fail fast before creating any DB records
        String photoUrl = fileStorageService.uploadImage(profilePicture, "medicare/doctors");

        // Step-2: Create the User (identity + DOCTOR role)
        Role doctorRole = roleRepository.findByName(RoleName.DOCTOR.name())
                .orElseThrow(() -> new ResourceNotFoundException("DOCTOR role not found, check role seeding"));

        Set<Role> roles = new HashSet<>();
        roles.add(doctorRole);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        // Step-3: Create the Doctor profile, linked to that User
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .yearsOfExperience(request.getYearsOfExperience())
                .consultationFee(request.getConsultationFee())
                .phoneNumber(request.getPhoneNumber())
                .qualifications(request.getQualifications())
                .available(true)
                .profilePictureUrl(photoUrl)
                .build();

        doctorRepository.save(doctor);

        return mapToDoctorResponse(user, doctor);
    }
}
