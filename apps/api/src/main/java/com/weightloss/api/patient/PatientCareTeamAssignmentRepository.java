package com.weightloss.api.patient;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientCareTeamAssignmentRepository extends JpaRepository<PatientCareTeamAssignment, UUID> {

    @Query("""
            select a.careTeamId
            from PatientCareTeamAssignment a
            join Patient p on p.id = a.patientId
            where a.patientId = :patientId
              and p.organizationId = :organizationId
              and a.status = 'ACTIVE'
              and a.startsAt <= :at
              and (a.endsAt is null or a.endsAt > :at)
            """)
    List<UUID> findActiveCareTeamIds(@Param("patientId") UUID patientId,
            @Param("organizationId") UUID organizationId, @Param("at") Instant at);
}
