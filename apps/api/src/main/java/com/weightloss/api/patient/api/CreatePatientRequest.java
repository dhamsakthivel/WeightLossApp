package com.weightloss.api.patient.api;

import java.time.LocalDate;
import java.util.UUID;

import com.weightloss.api.patient.PatientRecordStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePatientRequest(
        @NotBlank String legalGivenName,
        @NotBlank String legalFamilyName,
        @NotNull LocalDate dateOfBirth,
        @NotNull LocalDate enrollmentDate,
        @NotNull PatientRecordStatus recordStatus,
        String membershipStatus,
        UUID responsibleClinicianUserId,
        UUID careTeamId) {
}
