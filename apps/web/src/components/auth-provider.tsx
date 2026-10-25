"use client";

import { createContext, ReactNode, useContext, useEffect, useMemo, useState } from "react";
import { UserManager } from "oidc-client-ts";

import { createUserManager, isOidcConfigured } from "@/lib/oidc";

type AuthState = {
  accessToken: string | null;
  isConfigured: boolean;
  signIn: () => Promise<void>;
  completeSignIn: () => Promise<void>;
  signOut: () => Promise<void>;
};

const AuthContext = createContext<AuthState | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const configured = isOidcConfigured();
  const manager = useMemo<UserManager | null>(
    () => (configured && typeof window !== "undefined" ? createUserManager() : null),
    [configured],
  );
  const [accessToken, setAccessToken] = useState<string | null>(null);

  useEffect(() => {
    if (!manager) {
      return;
    }
    manager.getUser().then((user) => setAccessToken(user?.access_token ?? null));
  }, [manager]);

  const state: AuthState = {
    accessToken,
    isConfigured: configured,
    signIn: async () => {
      if (manager) {
        await manager.signinRedirect();
      }
    },
    completeSignIn: async () => {
      if (!manager) {
        return;
      }
      const user = await manager.signinRedirectCallback();
      setAccessToken(user.access_token);
    },
    signOut: async () => {
      if (manager) {
        await manager.signoutRedirect();
      }
    },
  };

  return <AuthContext.Provider value={state}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthState {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}
