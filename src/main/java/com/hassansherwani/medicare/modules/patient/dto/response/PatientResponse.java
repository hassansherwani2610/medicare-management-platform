package com.hassansherwani.medicare.modules.patient.dto.response;

import com.hassansherwani.medicare.modules.patient.enums.BloodGroup;
import com.hassansherwani.medicare.modules.patient.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String phoneNumber;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhoneNumber;
    private String profilePictureUrl;
}
