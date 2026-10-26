package com.weightloss.api.foundation.security;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * Enables the default-deny security chain only when no OIDC issuer is configured.
 */
final class OidcIssuerMissingCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !StringUtils.hasText(
                context.getEnvironment().getProperty("app.security.oidc.issuer-uri"));
    }
}
