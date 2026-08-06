package com.hassansherwani.medicare.modules.patient.controller;

import com.hassansherwani.medicare.modules.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PatientController(PatientService patientService, ObjectMapper objectMapper){
        this.patientService = patientService;
        this.objectMapper = objectMapper;
    }
}
