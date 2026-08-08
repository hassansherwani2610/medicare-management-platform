package com.hassansherwani.medicare.modules.doctor.dto.response;

import com.hassansherwani.medicare.modules.doctor.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String fullName;
    private String email;
    private Specialization specialization;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private BigDecimal consultationFee;
    private String phoneNumber;
    private String qualifications;
    private boolean available;
    private String profilePictureUrl;
}
