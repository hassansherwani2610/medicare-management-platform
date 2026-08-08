package com.hassansherwani.medicare.modules.doctor.serviceImpl;

import com.hassansherwani.medicare.common.util.FileStorageService;
import com.hassansherwani.medicare.modules.auth.entity.User;
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
    @Transactional
    public DoctorResponse registerDoctor(DoctorRegisterRequest request, MultipartFile profilePicture) {
        return null;
    }

    @Override
    public DoctorResponse getMyProfile(String email) {
        return null;
    }
}
