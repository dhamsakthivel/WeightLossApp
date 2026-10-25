package com.weightloss.api.patient;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.weightloss.api.foundation.access.CareTeamAccessPolicy;
import com.weightloss.api.foundation.identity.OrganizationMembershipRepository;

@ExtendWith(MockitoExtension.class)
class PatientRegistrationAuthorizationTest {

    @Mock
    private OrganizationMembershipRepository memberships;

    @Mock
    private CareTeamAccessPolicy careTeamAccess;

    @Test
    void allowsAuthorizedStaffForAnAuthorizedCareTeam() {
        UUID userId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        UUID careTeamId = UUID.randomUUID();
        when(memberships.existsByOrganizationIdAndUserIdAndRoleInAndStatus(
                eq(organizationId), eq(userId), any(), eq("ACTIVE"))).thenReturn(true);
        when(careTeamAccess.canAccess(userId, organizationId, careTeamId)).thenReturn(true);

        assertThatCode(() -> authorization().requireRegistrationAccess(userId, organizationId, command(careTeamId)))
                .doesNotThrowAnyException();
    }

    @Test
    void deniesAUserWithoutAnAuthorizedStaffRole() {
        when(memberships.existsByOrganizationIdAndUserIdAndRoleInAndStatus(any(), any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> authorization().requireRegistrationAccess(
                UUID.randomUUID(), UUID.randomUUID(), command(UUID.randomUUID())))
                .isInstanceOf(AccessDeniedException.class);
    }

    private PatientRegistrationAuthorization authorization() {
        return new PatientRegistrationAuthorization(memberships, careTeamAccess);
    }

    private CreatePatientCommand command(UUID careTeamId) {
        return new CreatePatientCommand("Avery", "Patient", LocalDate.of(1990, 1, 1), LocalDate.now(),
                PatientRecordStatus.ACTIVE, "ACTIVE", UUID.randomUUID(), careTeamId);
    }
}
