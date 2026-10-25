package com.weightloss.api.foundation.identity;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMembershipRepository extends JpaRepository<OrganizationMembership, UUID> {

    boolean existsByOrganizationIdAndUserIdAndRoleInAndStatus(
            UUID organizationId,
            UUID userId,
            Collection<OrganizationRole> roles,
            String status);
}
