package com.weightloss.api.foundation.access;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Default clinical-access rule for care-team-scoped resources.
 *
 * Later patient authorization composes this policy with patient-to-care-team
 * assignment. It intentionally does not handle break-glass access.
 */
@Service
public class CareTeamAccessPolicy {

    private final CareTeamMembershipRepository memberships;
    private final ClinicalAccessGrantRepository grants;
    private final Clock clock;

    public CareTeamAccessPolicy(CareTeamMembershipRepository memberships, ClinicalAccessGrantRepository grants,
            Clock clock) {
        this.memberships = memberships;
        this.grants = grants;
        this.clock = clock;
    }

    public boolean canAccess(UUID userId, UUID organizationId, UUID careTeamId) {
        Instant now = clock.instant();
        return memberships.hasActiveMembership(userId, organizationId, careTeamId, now)
                || grants.hasActiveGrant(userId, organizationId, careTeamId, now);
    }
}
