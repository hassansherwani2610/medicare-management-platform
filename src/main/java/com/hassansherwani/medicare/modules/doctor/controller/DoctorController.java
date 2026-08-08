package com.hassansherwani.medicare.modules.doctor.controller;

import com.hassansherwani.medicare.common.exception.ValidationException;
import com.hassansherwani.medicare.common.response.ApiResponse;
import com.hassansherwani.medicare.modules.doctor.dto.request.DoctorRegisterRequest;
import com.hassansherwani.medicare.modules.doctor.dto.response.DoctorResponse;
import com.hassansherwani.medicare.modules.doctor.service.DoctorService;
import com.hassansherwani.medicare.modules.patient.service.PatientService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public DoctorController(DoctorService doctorService, ObjectMapper objectMapper, Validator validator){
        this.doctorService = doctorService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    private void validateRequest(DoctorRegisterRequest request) {
        Set<ConstraintViolation<DoctorRegisterRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<DoctorRegisterRequest> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ValidationException(errors);
        }
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> registerDoctor(@RequestPart("data") String requestJson, @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
        return null;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getMyProfile(Authentication authentication) {
        return null;
    }
}
