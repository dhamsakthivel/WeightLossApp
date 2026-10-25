package com.weightloss.api.foundation.identity;

import java.util.Objects;

/**
 * The stable external identity key used by this application.
 *
 * JWT subjects are unique only within an issuer, so the pair is mandatory.
 */
public record OidcIdentity(String issuer, String subject) {

    public OidcIdentity {
        Objects.requireNonNull(issuer, "issuer is required");
        Objects.requireNonNull(subject, "subject is required");
        if (issuer.isBlank() || subject.isBlank()) {
            throw new IllegalArgumentException("issuer and subject must not be blank");
        }
    }
}
