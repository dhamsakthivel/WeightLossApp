package com.weightloss.api.foundation.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditEventServiceTest {

    @Mock
    private AuditEventRepository auditEvents;

    @Test
    void recordsAnOrganizationScopedEvent() {
        when(auditEvents.save(any(AuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID eventId = new AuditEventService(auditEvents,
                Clock.fixed(Instant.parse("2026-10-25T20:30:00Z"), ZoneOffset.UTC))
                .record(new AuditEventCommand(
                        UUID.randomUUID(),
                        "PATIENT_RECORD_CREATED",
                        UUID.randomUUID(),
                        "PATIENT",
                        UUID.randomUUID(),
                        "SUCCESS",
                        UUID.randomUUID(),
                        "{\"source\":\"test\"}"));

        assertThat(eventId).isNotNull();
        verify(auditEvents).save(any(AuditEvent.class));
    }
}
