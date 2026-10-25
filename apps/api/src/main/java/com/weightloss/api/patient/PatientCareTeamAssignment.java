package com.weightloss.api.patient;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_care_team_assignments")
public class PatientCareTeamAssignment {

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "care_team_id", nullable = false)
    private UUID careTeamId;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at")
    private Instant endsAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PatientCareTeamAssignment() {
    }

    PatientCareTeamAssignment(UUID id, UUID patientId, UUID careTeamId, String status, Instant startsAt,
            Instant endsAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.careTeamId = careTeamId;
        this.status = status;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
