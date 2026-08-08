package com.hassansherwani.medicare.modules.patient.service;

import com.hassansherwani.medicare.modules.patient.dto.request.PatientRegisterRequest;
import com.hassansherwani.medicare.modules.patient.dto.response.PatientResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PatientService {
    PatientResponse registerPatient(PatientRegisterRequest request, MultipartFile profilePicture);
    PatientResponse getMyProfile(String email);
}
