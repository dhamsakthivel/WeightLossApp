"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import { useAuth } from "@/components/auth-provider";

export default function AuthCallbackPage() {
  const { completeSignIn } = useAuth();
  const router = useRouter();
  const [message, setMessage] = useState("Completing sign-in…");

  useEffect(() => {
    completeSignIn()
      .then(() => router.replace("/staff/patients/new"))
      .catch(() => setMessage("Sign-in could not be completed."));
  }, [completeSignIn, router]);

  return <main><p role="status">{message}</p></main>;
}
