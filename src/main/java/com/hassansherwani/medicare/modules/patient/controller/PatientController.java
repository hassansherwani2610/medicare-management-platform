package com.hassansherwani.medicare.modules.patient.controller;

import com.hassansherwani.medicare.common.exception.ValidationException;
import com.hassansherwani.medicare.common.response.ApiResponse;
import com.hassansherwani.medicare.modules.patient.dto.request.PatientRegisterRequest;
import com.hassansherwani.medicare.modules.patient.dto.response.PatientResponse;
import com.hassansherwani.medicare.modules.patient.service.PatientService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public PatientController(PatientService patientService, ObjectMapper objectMapper, Validator validator){
        this.patientService = patientService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    private void validateRequest(PatientRegisterRequest request) {
        Set<ConstraintViolation<PatientRegisterRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<PatientRegisterRequest> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ValidationException(errors);
        }
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(@RequestPart("data") String requestJson, @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
        PatientRegisterRequest request = objectMapper.readValue(requestJson, PatientRegisterRequest.class);

        validateRequest(request);

        PatientResponse response = patientService.registerPatient(request, profilePicture);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient registered successfully", response));
    }
}
