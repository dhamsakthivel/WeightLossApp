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
authentication. The next increment introduces the organization/tenant model,
OIDC integration, server-side authorization policy, and append-only audit
events together.

## Local verification

```bash
cd apps/api && ./gradlew test
cd apps/web && npm run lint && npm run build
```

Use synthetic data only during development and testing.
