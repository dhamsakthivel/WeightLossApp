package com.weightloss.api.patient;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.weightloss.api.foundation.access.CareTeamAccessPolicy;

/**
 * Patient access requires both a current patient-to-team assignment and
 * authorization to that team. This prevents an organization role by itself
 * from becoming broad patient-record access.
 */
@Service
public class PatientAccessPolicy {

    private final PatientCareTeamAssignmentRepository patientCareTeams;
    private final CareTeamAccessPolicy careTeamAccess;
    private final Clock clock;

    public PatientAccessPolicy(PatientCareTeamAssignmentRepository patientCareTeams,
            CareTeamAccessPolicy careTeamAccess, Clock clock) {
        this.patientCareTeams = patientCareTeams;
        this.careTeamAccess = careTeamAccess;
        this.clock = clock;
    }

    public boolean canAccess(UUID userId, UUID organizationId, UUID patientId) {
        Instant now = clock.instant();
        return patientCareTeams.findActiveCareTeamIds(patientId, organizationId, now).stream()
                .anyMatch(careTeamId -> careTeamAccess.canAccess(userId, organizationId, careTeamId));
    }
}
