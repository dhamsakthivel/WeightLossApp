package com.weightloss.api.foundation.local;

import java.time.Instant;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Synthetic local development data only. IDs intentionally match the imported
 * Keycloak MA subject. Never enable this profile outside development.
 */
@Component
@Profile("local")
public class LocalDevelopmentDataInitializer implements ApplicationRunner {

    public static final String ORGANIZATION_ID = "20000000-0000-0000-0000-000000000001";
    public static final String MA_SUBJECT = "10000000-0000-0000-0000-000000000001";
    public static final String CARE_TEAM_ID = "30000000-0000-0000-0000-000000000001";

    private final JdbcTemplate jdbc;

    public LocalDevelopmentDataInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        Instant now = Instant.now();
        jdbc.update("""
                merge into organizations key(id)
                values (?, ?, ?, ?, ?, ?)
                """, ORGANIZATION_ID, "synthetic-practice", "Synthetic Practice", "ACTIVE", now, now);
        jdbc.update("""
                merge into user_accounts key(id)
                values (?, ?, ?, ?, ?, ?, ?)
                """, MA_SUBJECT, "http://localhost:8081/realms/weight-management-dev", MA_SUBJECT,
                "Synthetic Medical Assistant", "ACTIVE", now, now);
        jdbc.update("""
                merge into organization_memberships key(id)
                values (?, ?, ?, ?, ?, ?, ?)
                """, "40000000-0000-0000-0000-000000000001", ORGANIZATION_ID, MA_SUBJECT,
                "MEDICAL_ASSISTANT", "ACTIVE", now, now);
        jdbc.update("""
                merge into care_teams key(id)
                values (?, ?, ?, ?, ?, ?)
                """, CARE_TEAM_ID, ORGANIZATION_ID, "Synthetic Care Team", "ACTIVE", now, now);
        jdbc.update("""
                merge into care_team_memberships key(id)
                values (?, ?, ?, ?, ?, ?, ?, ?)
                """, "50000000-0000-0000-0000-000000000001", CARE_TEAM_ID, MA_SUBJECT,
                "ACTIVE", now, null, now, now);
    }
}
