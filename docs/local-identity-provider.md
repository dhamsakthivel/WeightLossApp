# Local Identity Provider

Development uses Keycloak only with synthetic accounts. It is not a production
identity-provider recommendation and must never contain patient information.

## Start Keycloak

Install Docker Desktop or Docker Engine, then run:

```bash
docker compose -f infra/docker-compose.local.yml up
```

Open `http://localhost:8081` and sign in with the local bootstrap credentials.
Override the insecure development defaults before starting:

```bash
export KEYCLOAK_ADMIN_USERNAME=local-admin
export KEYCLOAK_ADMIN_PASSWORD='a-local-only-password'
```

The imported realm is `weight-management-dev`; its browser client is
`weight-management-web`, configured for authorization-code flow with PKCE.

## API configuration

Set this environment variable before starting Spring Boot:

```bash
export APP_SECURITY_OIDC_ISSUER_URI=http://localhost:8081/realms/weight-management-dev
```

This activates Spring Security's JWT resource-server mode. A signed token still
requires an active local `user_accounts` record and appropriate organization,
role, and care-team authorization.

## Why this exists

The React application needs a genuine OIDC token to call protected APIs. Using
local Keycloak lets us learn the authorization-code + PKCE flow without adding
a mock production-authentication bypass. The local environment currently
cannot run Docker, so this compose configuration is documented but untested in
the cloud workspace.
