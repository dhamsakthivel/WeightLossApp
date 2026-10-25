package com.weightloss.api.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weightloss.api.foundation.audit.AuditEventService;

@ExtendWith(MockitoExtension.class)
class PatientRegistrationServiceTest {

    @Mock
    private PatientRepository patients;

    @Mock
    private PatientCareTeamAssignmentRepository patientCareTeams;

    @Mock
    private AuditEventService auditEvents;

    @Test
    void registersAnActivePatientWithTeamAssignmentAndAuditEvent() {
        when(patients.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient patient = service().register(UUID.randomUUID(), UUID.randomUUID(),
                command(PatientRecordStatus.ACTIVE, UUID.randomUUID(), UUID.randomUUID()));

        assertThat(patient.getInternalId()).startsWith("PAT-");
        assertThat(patient.getRecordStatus()).isEqualTo(PatientRecordStatus.ACTIVE);
        verify(patientCareTeams).save(any(PatientCareTeamAssignment.class));
        verify(auditEvents).record(any());
    }

    @Test
    void rejectsActivationWithoutClinicianAndCareTeam() {
        assertThatThrownBy(() -> service().register(UUID.randomUUID(), UUID.randomUUID(),
                command(PatientRecordStatus.ACTIVE, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("responsible clinician and care team");

        verify(patients, never()).save(any());
        verify(auditEvents, never()).record(any());
    }

    private PatientRegistrationService service() {
        return new PatientRegistrationService(patients, patientCareTeams, auditEvents,
                Clock.fixed(Instant.parse("2026-10-25T21:00:00Z"), ZoneOffset.UTC));
    }

    private CreatePatientCommand command(PatientRecordStatus status, UUID clinicianId, UUID careTeamId) {
        return new CreatePatientCommand(
                "Avery",
                "Patient",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2026, 10, 25),
                status,
                "ACTIVE",
                clinicianId,
                careTeamId);
    }
}
