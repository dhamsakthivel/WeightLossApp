package com.weightloss.api.foundation.access;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CareTeamAccessPolicyTest {

    @Mock
    private CareTeamMembershipRepository memberships;

    @Mock
    private ClinicalAccessGrantRepository grants;

    private final UUID userId = UUID.randomUUID();
    private final UUID organizationId = UUID.randomUUID();
    private final UUID careTeamId = UUID.randomUUID();

    @Test
    void allowsAnActiveCareTeamMemberWithoutCheckingAnExceptionGrant() {
        Instant now = Instant.parse("2026-10-25T18:00:00Z");
        when(memberships.hasActiveMembership(eq(userId), eq(organizationId), eq(careTeamId), any()))
                .thenReturn(true);

        boolean allowed = policy(now).canAccess(userId, organizationId, careTeamId);

        assertThat(allowed).isTrue();
        verify(grants, never()).hasActiveGrant(any(), any(), any(), any());
    }

    @Test
    void allowsAValidPhysicianApprovedGrantWhenThereIsNoTeamMembership() {
        Instant now = Instant.parse("2026-10-25T18:00:00Z");
        when(memberships.hasActiveMembership(eq(userId), eq(organizationId), eq(careTeamId), any()))
                .thenReturn(false);
        when(grants.hasActiveGrant(eq(userId), eq(organizationId), eq(careTeamId), any()))
                .thenReturn(true);

        assertThat(policy(now).canAccess(userId, organizationId, careTeamId)).isTrue();
    }

    @Test
    void deniesWhenNeitherCareTeamMembershipNorGrantIsActive() {
        Instant now = Instant.parse("2026-10-25T18:00:00Z");
        when(memberships.hasActiveMembership(eq(userId), eq(organizationId), eq(careTeamId), any()))
                .thenReturn(false);
        when(grants.hasActiveGrant(eq(userId), eq(organizationId), eq(careTeamId), any()))
                .thenReturn(false);

        assertThat(policy(now).canAccess(userId, organizationId, careTeamId)).isFalse();
    }

    private CareTeamAccessPolicy policy(Instant now) {
        return new CareTeamAccessPolicy(memberships, grants, Clock.fixed(now, ZoneOffset.UTC));
    }
}
