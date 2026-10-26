"use client";

import { useEffect } from "react";

import { createUserManager } from "@/lib/oidc";

export default function SilentAuthCallbackPage() {
  useEffect(() => {
    void createUserManager().signinSilentCallback();
  }, []);

  return null;
}
