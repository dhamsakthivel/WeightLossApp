"use client";

import { PatientRegistrationForm } from "@/components/patient-registration-form";
import { useAuth } from "@/components/auth-provider";
import { CreatePatientRequest, registerPatient } from "@/lib/patient-api";

const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL;
const organizationId = process.env.NEXT_PUBLIC_DEMO_ORGANIZATION_ID;

export function PatientRegistrationWorkspace() {
  const { accessToken, isConfigured, signIn, signOut } = useAuth();
  const canSubmit = Boolean(accessToken && apiBaseUrl && organizationId);

  async function submit(request: CreatePatientRequest) {
    if (!accessToken || !apiBaseUrl || !organizationId) {
      throw new Error("An authenticated session and API configuration are required.");
    }
    return registerPatient(apiBaseUrl, organizationId, accessToken, request);
  }

  return (
    <>
      {accessToken ? (
        <button type="button" onClick={() => void signOut()}>
          Sign out
        </button>
      ) : (
        <button type="button" onClick={() => void signIn()} disabled={!isConfigured}>
          Sign in
        </button>
      )}
      {!isConfigured && (
        <p className="notice" role="status">
          Local identity configuration is required before sign-in can begin.
        </p>
      )}
      <PatientRegistrationForm
        isAuthenticated={canSubmit}
        careTeams={[]}
        onSubmit={canSubmit ? submit : undefined}
      />
    </>
  );
}
