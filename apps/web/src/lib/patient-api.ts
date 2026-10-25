export type PatientRecordStatus =
  | "ADMINISTRATIVE_INTAKE"
  | "ACTIVE"
  | "INACTIVE"
  | "DISCHARGED";

export type CreatePatientRequest = {
  legalGivenName: string;
  legalFamilyName: string;
  dateOfBirth: string;
  enrollmentDate: string;
  recordStatus: PatientRecordStatus;
  membershipStatus?: string;
  responsibleClinicianUserId?: string;
  careTeamId?: string;
};

export type PatientRegistrationResponse = {
  id: string;
  internalId: string;
  recordStatus: PatientRecordStatus;
};

export async function registerPatient(
  apiBaseUrl: string,
  organizationId: string,
  accessToken: string,
  request: CreatePatientRequest,
): Promise<PatientRegistrationResponse> {
  const response = await fetch(
    `${apiBaseUrl}/api/v1/organizations/${organizationId}/patients`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    },
  );

  if (!response.ok) {
    throw new Error("Patient registration could not be completed.");
  }

  return response.json() as Promise<PatientRegistrationResponse>;
}
