package com.weightloss.api.foundation.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;

@SpringBootTest
class AuthenticatedUserResolverTest {

    @Autowired
    private AuthenticatedUserResolver resolver;

    @Autowired
    private UserAccountRepository users;

    @Test
    void resolvesAnActiveAccountFromIssuerAndSubject() {
        UUID userId = UUID.randomUUID();
        users.save(new UserAccount(userId, "https://identity.example.test", "staff-123",
                "Example Staff", "ACTIVE", Instant.now(), Instant.now()));

        UUID resolvedUserId = resolver.resolveActiveUserId(jwt("https://identity.example.test", "staff-123"));

        assertThat(resolvedUserId).isEqualTo(userId);
    }

    @Test
    void deniesUnknownOrInactiveExternalIdentities() {
        assertThatThrownBy(() -> resolver.resolveActiveUserId(jwt("https://identity.example.test", "unknown")))
                .isInstanceOf(AccessDeniedException.class);
    }

    private Jwt jwt(String issuer, String subject) {
        return Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .issuer(issuer)
                .subject(subject)
                .claim("scope", "openid")
                .build();
    }
}
