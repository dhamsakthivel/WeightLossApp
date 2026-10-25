package com.weightloss.api.patient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tenant-scoped longitudinal monitoring record.
 *
 * This entity intentionally has no public controller yet. API exposure waits
 * for composed patient authorization and audit-event handling.
 */
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "internal_id", nullable = false, length = 64)
    private String internalId;

    @Column(name = "legal_given_name", nullable = false, length = 100)
    private String legalGivenName;

    @Column(name = "legal_family_name", nullable = false, length = 100)
    private String legalFamilyName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "record_status", nullable = false, length = 32)
    private String recordStatus;

    @Column(name = "membership_status", length = 32)
    private String membershipStatus;

    @Column(name = "responsible_clinician_user_id")
    private UUID responsibleClinicianUserId;

    @Column(name = "status_reason", length = 500)
    private String statusReason;

    @Column(name = "status_effective_at", nullable = false)
    private Instant statusEffectiveAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Patient() {
    }
}
