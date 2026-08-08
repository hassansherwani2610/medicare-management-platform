package com.hassansherwani.medicare.modules.doctor.dto.request;

import com.hassansherwani.medicare.modules.doctor.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DoctorRegisterRequest {

    // Account fields (feeds into User)
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Temporary password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    // Doctor profile fields
    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 60, message = "Years of experience seems unrealistic")
    private Integer yearsOfExperience;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Consultation fee must be greater than 0")
    private BigDecimal consultationFee;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?=.*\\d)[\\d+\\-\\s]{7,20}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Qualifications are required")
    @Size(max = 500)
    private String qualifications;
}