package com.weightloss.api.foundation.identity;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticatedUserResolver {

    private static final String ACTIVE = "ACTIVE";

    private final UserAccountRepository userAccounts;

    public AuthenticatedUserResolver(UserAccountRepository userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Transactional(readOnly = true)
    public UUID resolveActiveUserId(Jwt jwt) {
        OidcIdentity identity = new OidcIdentity(jwt.getIssuer().toString(), jwt.getSubject());

        return userAccounts.findByIssuerAndSubjectAndStatus(identity.issuer(), identity.subject(), ACTIVE)
                .map(UserAccount::getId)
                .orElseThrow(() -> new AccessDeniedException("The authenticated identity has no active application account"));
    }
}
