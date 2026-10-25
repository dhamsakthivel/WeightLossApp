# Weight Management Platform

Learning-focused, multi-organization medical weight-management monitoring platform.

## Foundation architecture

- `apps/web`: Next.js, React, and TypeScript responsive web application.
- `apps/api`: Java 21 and Spring Boot API.

The frontend is responsible for accessible user experience. The backend remains
the authority for authentication, tenant isolation, care-team authorization,
clinical workflows, and audit logging.

## Increment A

This initial increment creates the two application boundaries and a non-PHI API
availability endpoint:

```text
GET /api/v1/system/status
```

It deliberately does not expose patient data or implement temporary mock
authentication. It now includes a PostgreSQL/Flyway tenant and audit schema,
plus a default-deny Spring Security policy. OIDC integration will replace the
temporary deny-all policy before any protected endpoint is introduced.

## Learning checkpoint: why the tenant schema comes early

Every future PHI table will include an organization identifier. The API will
use that identifier with role, care-team, grant, and break-glass policy checks
to prevent one practice from accessing another practice's data. Flyway tracks
database changes as reviewed, ordered migrations rather than allowing the ORM
to change production schema automatically.

## Local verification

```bash
cd apps/api && ./gradlew test
cd apps/web && npm run lint && npm run build
```

Use synthetic data only during development and testing.
