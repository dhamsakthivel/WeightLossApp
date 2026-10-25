# Implementation Progress

This is the running, version-controlled reference for the full-stack learning
build. It supplements the approved product, security, and architecture
specifications.

## Confirmed architecture

- Frontend: Next.js, React, and TypeScript responsive web/PWA.
- Backend: Java 21, Spring Boot modular monolith, Gradle, OpenAPI.
- Data: PostgreSQL, Flyway migrations, JPA.
- Security: external OIDC, mandatory passkey/TOTP MFA, server-side
  organization/role/care-team authorization, append-only audit events.
- Deployment target: practice-owned AWS account using private application and
  data services. PHI processing remains gated on approved vendors, contracts,
  and operational controls.

## Completed

### Increment A — application and tenant foundation

- Created `apps/web` with Next.js, React, TypeScript, linting, and production
  build verification.
- Created `apps/api` with Spring Boot, Java 21, Gradle wrapper, and actuator.
- Added `GET /api/v1/system/status`, a deliberately non-PHI availability
  endpoint with `Cache-Control: no-store`.
- Added Spring Security default-deny behavior. Only the status and health
  routes are available until OIDC authentication is configured.
- Added Flyway migration `V1`:
  - `organizations` is the root tenant boundary.
  - `audit_events` is the append-only audit-data foundation.
- Added JPA organization model and H2-backed test configuration. Production
  will use PostgreSQL; H2 exists only to validate migrations and mappings in
  automated tests where Docker/Testcontainers is unavailable.
- Added Flyway migration `V2` and JPA models for:
  - verified OIDC identity mapping (`issuer` + `subject`);
  - organization memberships;
  - initial staff roles: physician, medical assistant, administrator, and
    privacy reviewer.
- Added a migration test that verifies the tenant, identity, membership, and
  audit tables are available together.
- Added the OIDC resource-server dependency and a server-side identity
  resolver. It maps only verified `issuer + subject` claims to an existing,
  active local account and denies unknown identities.
- Added Flyway migration `V3` and care-team access model:
  - care teams and time-bounded active staff memberships;
  - physician-approved, time-bounded care-team clinical-access grants;
  - a centralized policy that allows access only through current membership or
    a current grant.
- Added policy tests for direct membership, approved grant, and denial.
- Verified:
  - `apps/api`: `./gradlew test`
  - `apps/web`: `npm run lint`
  - `apps/web`: `npm run build`

## What you learned

The browser is not a security boundary. The React application provides the
user experience, while Spring Boot decides whether a request is allowed.
Organization scope is introduced before patient data so later clinical tables
cannot accidentally omit their tenant boundary. Flyway is used instead of
automatic ORM schema changes so every database change is reviewed, ordered,
and reproducible.

## Next

1. Replace the temporary default-deny policy with configured OIDC JWT
   authentication.
2. Add patient records and patient-to-care-team assignment.
3. Compose patient authorization from organization, identity, role,
   care-team, grant, and patient assignment.
