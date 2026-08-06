package com.hassansherwani.medicare.modules.patient.dto.request;

import com.hassansherwani.medicare.modules.patient.enums.BloodGroup;
import com.hassansherwani.medicare.modules.patient.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRegisterRequest {

    // ---- Account fields (feeds into User) ----
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    // ---- Patient profile fields ----
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private BloodGroup bloodGroup; // Optional

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?=.*\\d)[\\d+\\-\\s]{7,20}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String address; // Optional

    @NotBlank(message = "Emergency contact name is required")
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone number is required")
    @Pattern(regexp = "^(?=.*\\d)[\\d+\\-\\s]{7,20}$", message = "Invalid emergency contact phone number format")
    private String emergencyContactPhoneNumber;
}