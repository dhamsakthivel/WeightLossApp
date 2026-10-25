# Deployment Configuration

## OIDC issuer

The API starts in default-deny mode until an approved identity provider is
configured. In a protected environment, set:

```text
APP_SECURITY_OIDC_ISSUER_URI=https://identity.practice.example
```

Spring maps this environment variable to `app.security.oidc.issuer-uri`.

The issuer must:

1. be approved for the practice's PHI and contractual requirements;
2. support OIDC discovery, signed JWT access tokens, and mandatory
   passkey/WebAuthn or TOTP MFA;
3. issue a stable `iss` and `sub` pair;
4. be configured only through deployment secrets/configuration, never source
   control.

An authenticated token grants no clinical access by itself. Its verified
`iss`/`sub` must map to an active local account, organization membership, and
later the applicable care-team or physician-approved grant.
