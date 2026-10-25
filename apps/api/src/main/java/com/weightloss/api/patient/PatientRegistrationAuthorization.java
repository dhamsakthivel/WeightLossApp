package com.weightloss.api.patient;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.weightloss.api.foundation.access.CareTeamAccessPolicy;
import com.weightloss.api.foundation.identity.OrganizationMembershipRepository;
import com.weightloss.api.foundation.identity.OrganizationRole;

@Service
public class PatientRegistrationAuthorization {

    private static final String ACTIVE = "ACTIVE";
    private static final List<OrganizationRole> REGISTRATION_ROLES = List.of(
            OrganizationRole.PHYSICIAN,
            OrganizationRole.MEDICAL_ASSISTANT);

    private final OrganizationMembershipRepository memberships;
    private final CareTeamAccessPolicy careTeamAccess;

    public PatientRegistrationAuthorization(OrganizationMembershipRepository memberships,
            CareTeamAccessPolicy careTeamAccess) {
        this.memberships = memberships;
        this.careTeamAccess = careTeamAccess;
    }

    public void requireRegistrationAccess(UUID userId, UUID organizationId, CreatePatientCommand command) {
        boolean hasRole = memberships.existsByOrganizationIdAndUserIdAndRoleInAndStatus(
                organizationId, userId, REGISTRATION_ROLES, ACTIVE);
        if (!hasRole) {
            throw new AccessDeniedException("The user is not authorized to register patients for this organization");
        }

        if (command.careTeamId() != null
                && !careTeamAccess.canAccess(userId, organizationId, command.careTeamId())) {
            throw new AccessDeniedException("The user is not authorized for the selected care team");
        }
    }
}
