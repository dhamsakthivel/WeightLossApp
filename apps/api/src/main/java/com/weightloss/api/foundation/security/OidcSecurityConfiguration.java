package com.weightloss.api.foundation.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Enabled only after the practice configures an approved OIDC issuer.
 *
 * The issuer URI is intentionally supplied through deployment configuration,
 * not source control. Signature and issuer validation happen before the local
 * issuer/subject-to-user-account lookup is used by protected API features.
 */
@Configuration
@ConditionalOnProperty(prefix = "app.security.oidc", name = "issuer-uri")
public class OidcSecurityConfiguration {

    @Bean
    JwtDecoder jwtDecoder(@Value("${app.security.oidc.issuer-uri}") String issuerUri) {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    SecurityFilterChain oidcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/system/status", "/actuator/health").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwt -> {
                }))
                .build();
    }
}
