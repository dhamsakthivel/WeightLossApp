package com.weightloss.api.patient;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weightloss.api.foundation.audit.AuditEventCommand;
import com.weightloss.api.foundation.audit.AuditEventService;

@Service
public class PatientRegistrationService {

    private final PatientRepository patients;
    private final PatientCareTeamAssignmentRepository patientCareTeams;
    private final AuditEventService auditEvents;
    private final Clock clock;

    public PatientRegistrationService(PatientRepository patients,
            PatientCareTeamAssignmentRepository patientCareTeams,
            AuditEventService auditEvents,
            Clock clock) {
        this.patients = patients;
        this.patientCareTeams = patientCareTeams;
        this.auditEvents = auditEvents;
        this.clock = clock;
    }

    @Transactional
    public Patient register(UUID organizationId, UUID actorUserId, CreatePatientCommand command) {
        require(command.legalGivenName(), "legal given name");
        require(command.legalFamilyName(), "legal family name");
        Objects.requireNonNull(command.dateOfBirth(), "date of birth is required");
        Objects.requireNonNull(command.enrollmentDate(), "enrollment date is required");
        Objects.requireNonNull(command.recordStatus(), "record status is required");

        if (command.recordStatus() == PatientRecordStatus.ACTIVE
                && (command.responsibleClinicianUserId() == null || command.careTeamId() == null)) {
            throw new IllegalArgumentException("an active patient requires a responsible clinician and care team");
        }

        Instant now = clock.instant();
        UUID patientId = UUID.randomUUID();
        String internalId = "PAT-" + patientId;
        Patient patient = patients.save(new Patient(
                patientId,
                organizationId,
                internalId,
                command.legalGivenName().trim(),
                command.legalFamilyName().trim(),
                command.dateOfBirth(),
                command.enrollmentDate(),
                command.recordStatus(),
                command.membershipStatus(),
                command.responsibleClinicianUserId(),
                null,
                now,
                now,
                now));

        if (command.careTeamId() != null) {
            patientCareTeams.save(new PatientCareTeamAssignment(
                    UUID.randomUUID(), patientId, command.careTeamId(), "ACTIVE", now, null, now, now));
        }

        auditEvents.record(new AuditEventCommand(
                organizationId,
                "PATIENT_RECORD_CREATED",
                actorUserId,
                "PATIENT",
                patientId,
                "SUCCESS",
                UUID.randomUUID(),
                "{\"recordStatus\":\"" + command.recordStatus().name() + "\"}"));

        return patient;
    }

    private void require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}
