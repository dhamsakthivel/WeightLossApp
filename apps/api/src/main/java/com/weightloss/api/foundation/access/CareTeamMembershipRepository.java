package com.weightloss.api.foundation.access;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CareTeamMembershipRepository extends JpaRepository<CareTeamMembership, UUID> {

    @Query("""
            select count(m) > 0
            from CareTeamMembership m
            join CareTeam t on t.id = m.careTeamId
            where m.userId = :userId
              and m.careTeamId = :careTeamId
              and t.organizationId = :organizationId
              and t.status = 'ACTIVE'
              and m.status = 'ACTIVE'
              and m.startsAt <= :at
              and (m.endsAt is null or m.endsAt > :at)
            """)
    boolean hasActiveMembership(@Param("userId") UUID userId, @Param("organizationId") UUID organizationId,
            @Param("careTeamId") UUID careTeamId, @Param("at") Instant at);
}
