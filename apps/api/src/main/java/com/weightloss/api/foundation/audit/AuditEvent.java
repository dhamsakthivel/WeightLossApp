package com.weightloss.api.foundation.audit;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Immutable application audit record. No update or delete repository methods
 * are exposed by the audit service.
 */
@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    private UUID id;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @Column(name = "resource_type", nullable = false, length = 100)
    private String resourceType;

    @Column(name = "resource_id")
    private UUID resourceId;

    @Column(nullable = false, length = 32)
    private String outcome;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "details_json", nullable = false)
    private String detailsJson;

    protected AuditEvent() {
    }

    AuditEvent(UUID id, UUID organizationId, String eventType, UUID actorUserId, String resourceType,
            UUID resourceId, String outcome, Instant occurredAt, UUID correlationId, String detailsJson) {
        this.id = id;
        this.organizationId = organizationId;
        this.eventType = eventType;
        this.actorUserId = actorUserId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.outcome = outcome;
        this.occurredAt = occurredAt;
        this.correlationId = correlationId;
        this.detailsJson = detailsJson;
    }
}
