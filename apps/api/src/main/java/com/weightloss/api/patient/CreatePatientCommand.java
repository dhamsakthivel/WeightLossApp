package com.weightloss.api.patient;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePatientCommand(
        String legalGivenName,
        String legalFamilyName,
        LocalDate dateOfBirth,
        LocalDate enrollmentDate,
        PatientRecordStatus recordStatus,
        String membershipStatus,
        UUID responsibleClinicianUserId,
        UUID careTeamId) {
}
