package com.hassansherwani.medicare.modules.doctor.service;

import com.hassansherwani.medicare.modules.doctor.dto.request.DoctorRegisterRequest;
import com.hassansherwani.medicare.modules.doctor.dto.response.DoctorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DoctorService {
    DoctorResponse registerDoctor(DoctorRegisterRequest request, MultipartFile profilePicture);
    DoctorResponse getMyProfile(String email);
}
