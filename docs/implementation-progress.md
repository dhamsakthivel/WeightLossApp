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
- Added append-only audit-event write service and a tested, organization-scoped
  audit command. The service records actor, resource, outcome, correlation ID,
  and JSON detail reference for future PHI workflow events.
- Began the Patient Registration vertical slice:
  - server-generated opaque patient internal ID;
  - validation that active registration needs a responsible clinician and care
    team;
  - atomic patient/care-team assignment creation plus audit-event write;
  - unit tests for successful registration and rejected incomplete activation.
- Added protected registration API contract:
  - `POST /api/v1/organizations/{organizationId}/patients`;
  - verified OIDC identity must map to an active local account;
  - only active physician or MA organization membership is eligible;
  - actor must have access to the selected care team;
  - endpoint stays unreachable until the approved OIDC issuer is configured.
- Added local Keycloak development configuration:
  - realm import, synthetic-only browser client, authorization-code flow, and
    PKCE;
  - compose file and setup guide for a real local issuer;
  - the cloud workspace lacks Docker, so this configuration is not run here.
- **Cloud Keycloak reminder:** Docker is unavailable in this Cloud Agent.
  A Docker-free Keycloak distribution/start configuration can be added to the
  dashboard-managed environment after `KEYCLOAK_ADMIN_PASSWORD` is supplied as
  a Cloud environment secret. Do not use the compose development password or
  any PHI in that environment.
- **Cloud Keycloak validation:** with the environment secret supplied, the
  Docker-free Keycloak 26.8.1 distribution started successfully on port 8081
  and imported the synthetic `weight-management-dev` realm. The runnable
  installation and start scripts are under `scripts/`.
- Added the React Patient Registration screen and typed API client:
  - accessible identity/enrollment form;
  - active-status validation cues for clinician and care-team assignment;
  - typed protected API request/response contract;
  - submission remains disabled until real OIDC session integration supplies a
    token and authorized care teams.
- Added React OIDC integration:
  - Authorization Code + PKCE client using `oidc-client-ts`;
  - sign-in/sign-out and callback route;
  - access token forwarded only to the protected API client;
  - `.env.example` documents the required synthetic local configuration.
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

1. Run Keycloak and PostgreSQL locally/cloud with synthetic seed data, then
   verify real registration end-to-end.
2. Load authorized care teams into the registration form.
3. Add patient list and profile correction workflow with role checks.
