package com.weightloss.api.foundation.audit;

import java.time.Clock;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class AuditEventService {

    private final AuditEventRepository auditEvents;
    private final Clock clock;

    public AuditEventService(AuditEventRepository auditEvents, Clock clock) {
        this.auditEvents = auditEvents;
        this.clock = clock;
    }

    public UUID record(AuditEventCommand command) {
        UUID id = UUID.randomUUID();
        auditEvents.save(new AuditEvent(
                id,
                command.organizationId(),
                command.eventType(),
                command.actorUserId(),
                command.resourceType(),
                command.resourceId(),
                command.outcome(),
                clock.instant(),
                command.correlationId(),
                command.detailsJson()));
        return id;
    }
}
