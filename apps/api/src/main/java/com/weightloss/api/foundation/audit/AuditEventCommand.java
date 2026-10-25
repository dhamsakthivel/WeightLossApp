package com.weightloss.api.foundation.audit;

import java.util.Objects;
import java.util.UUID;

public record AuditEventCommand(
        UUID organizationId,
        String eventType,
        UUID actorUserId,
        String resourceType,
        UUID resourceId,
        String outcome,
        UUID correlationId,
        String detailsJson) {

    public AuditEventCommand {
        Objects.requireNonNull(organizationId, "organizationId is required for PHI audit events");
        Objects.requireNonNull(eventType, "eventType is required");
        Objects.requireNonNull(resourceType, "resourceType is required");
        Objects.requireNonNull(outcome, "outcome is required");
        Objects.requireNonNull(detailsJson, "detailsJson is required");
    }
}
