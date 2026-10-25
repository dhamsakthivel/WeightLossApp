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
- Added a safe OIDC security-mode switch:
  - without an issuer, the API remains default-deny;
  - with `APP_SECURITY_OIDC_ISSUER_URI`, Spring validates JWT signatures and
    issuer claims before protected API access.
- Added deployment configuration guidance. The identity provider remains a
  practice/vendor decision and is not hard-coded.
- Added Flyway migration `V4` and initial patient domain:
  - tenant-scoped opaque internal ID, legal identity, enrollment, status, and
    responsible clinician fields;
  - time-bounded patient-to-care-team assignments.
- Added composed patient authorization. A user needs both an active
  patient-to-care-team assignment and access to at least one assigned care
  team through membership or an approved grant.
- No patient endpoint exists yet; patient data remains unexposed until audit
  writes and protected API handling are added.
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

1. Add append-only audit-event write service and protected patient API
   handling.
2. Add patient record creation and correction workflows with role checks.
