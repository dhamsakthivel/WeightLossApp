package com.weightloss.api.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weightloss.api.foundation.access.CareTeamAccessPolicy;

@ExtendWith(MockitoExtension.class)
class PatientAccessPolicyTest {

    @Mock
    private PatientCareTeamAssignmentRepository patientCareTeams;

    @Mock
    private CareTeamAccessPolicy careTeamAccess;

    private final UUID userId = UUID.randomUUID();
    private final UUID organizationId = UUID.randomUUID();
    private final UUID patientId = UUID.randomUUID();

    @Test
    void allowsOnlyWhenPatientAssignmentAndCareTeamAuthorizationBothExist() {
        UUID careTeamId = UUID.randomUUID();
        when(patientCareTeams.findActiveCareTeamIds(eq(patientId), eq(organizationId), any()))
                .thenReturn(List.of(careTeamId));
        when(careTeamAccess.canAccess(userId, organizationId, careTeamId)).thenReturn(true);

        assertThat(policy().canAccess(userId, organizationId, patientId)).isTrue();
    }

    @Test
    void deniesWhenThePatientHasNoActiveTeamAssignment() {
        when(patientCareTeams.findActiveCareTeamIds(eq(patientId), eq(organizationId), any()))
                .thenReturn(List.of());

        assertThat(policy().canAccess(userId, organizationId, patientId)).isFalse();
    }

    @Test
    void deniesWhenNoAssignedTeamAuthorizesTheUser() {
        UUID careTeamId = UUID.randomUUID();
        when(patientCareTeams.findActiveCareTeamIds(eq(patientId), eq(organizationId), any()))
                .thenReturn(List.of(careTeamId));
        when(careTeamAccess.canAccess(userId, organizationId, careTeamId)).thenReturn(false);

        assertThat(policy().canAccess(userId, organizationId, patientId)).isFalse();
        verify(careTeamAccess).canAccess(userId, organizationId, careTeamId);
    }

    private PatientAccessPolicy policy() {
        return new PatientAccessPolicy(patientCareTeams, careTeamAccess,
                Clock.fixed(Instant.parse("2026-10-25T20:00:00Z"), ZoneOffset.UTC));
    }
}
