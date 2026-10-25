import { UserManager, WebStorageStateStore } from "oidc-client-ts";

function required(value: string | undefined, name: string): string {
  if (!value) {
    throw new Error(`${name} must be configured to enable sign-in.`);
  }
  return value;
}

export function createUserManager(): UserManager {
  return new UserManager({
    authority: required(process.env.NEXT_PUBLIC_OIDC_AUTHORITY, "NEXT_PUBLIC_OIDC_AUTHORITY"),
    client_id: required(process.env.NEXT_PUBLIC_OIDC_CLIENT_ID, "NEXT_PUBLIC_OIDC_CLIENT_ID"),
    redirect_uri: required(process.env.NEXT_PUBLIC_OIDC_REDIRECT_URI, "NEXT_PUBLIC_OIDC_REDIRECT_URI"),
    post_logout_redirect_uri: process.env.NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI,
    response_type: "code",
    scope: "openid profile email",
    userStore: new WebStorageStateStore({ store: window.sessionStorage }),
  });
}

export function isOidcConfigured(): boolean {
  return Boolean(
    process.env.NEXT_PUBLIC_OIDC_AUTHORITY &&
      process.env.NEXT_PUBLIC_OIDC_CLIENT_ID &&
      process.env.NEXT_PUBLIC_OIDC_REDIRECT_URI,
  );
}
