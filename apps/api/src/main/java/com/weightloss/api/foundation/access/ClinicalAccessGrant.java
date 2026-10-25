package com.weightloss.api.foundation.access;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A time-bounded, physician-approved exception to default care-team access.
 * This does not replace emergency break-glass access, which is separately
 * reasoned, shorter lived, and independently reviewed.
 */
@Entity
@Table(name = "clinical_access_grants")
public class ClinicalAccessGrant {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "grantee_user_id", nullable = false)
    private UUID granteeUserId;

    @Column(name = "care_team_id", nullable = false)
    private UUID careTeamId;

    @Column(name = "approved_by_user_id", nullable = false)
    private UUID approvedByUserId;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(name = "effective_at", nullable = false)
    private Instant effectiveAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ClinicalAccessGrant() {
    }
}
