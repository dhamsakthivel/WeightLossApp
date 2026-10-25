package com.weightloss.api.foundation.access;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClinicalAccessGrantRepository extends JpaRepository<ClinicalAccessGrant, UUID> {

    @Query("""
            select count(g) > 0
            from ClinicalAccessGrant g
            where g.granteeUserId = :userId
              and g.organizationId = :organizationId
              and g.careTeamId = :careTeamId
              and g.status = 'ACTIVE'
              and g.effectiveAt <= :at
              and g.expiresAt > :at
            """)
    boolean hasActiveGrant(@Param("userId") UUID userId, @Param("organizationId") UUID organizationId,
            @Param("careTeamId") UUID careTeamId, @Param("at") Instant at);
}
