package com.weightloss.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ApiApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void appliesTenantIdentityAndAuditMigrations() {
		Integer tableCount = jdbcTemplate.queryForObject(
				"""
				select count(*)
				from information_schema.tables
				where table_name in (
					'organizations',
					'user_accounts',
					'organization_memberships',
					'audit_events',
					'care_teams',
					'care_team_memberships',
					'clinical_access_grants'
				)
				""",
				Integer.class);

		org.assertj.core.api.Assertions.assertThat(tableCount).isEqualTo(7);
	}

}
