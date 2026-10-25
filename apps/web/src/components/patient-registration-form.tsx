"use client";

import { FormEvent, useState } from "react";

import {
  CreatePatientRequest,
  PatientRegistrationResponse,
  PatientRecordStatus,
} from "@/lib/patient-api";

type CareTeamOption = {
  id: string;
  name: string;
};

type Props = {
  isAuthenticated: boolean;
  careTeams: CareTeamOption[];
  onSubmit?: (
    request: CreatePatientRequest,
  ) => Promise<PatientRegistrationResponse>;
};

const statuses: PatientRecordStatus[] = [
  "ADMINISTRATIVE_INTAKE",
  "ACTIVE",
];

export function PatientRegistrationForm({
  isAuthenticated,
  careTeams,
  onSubmit,
}: Props) {
  const [status, setStatus] =
    useState<PatientRecordStatus>("ADMINISTRATIVE_INTAKE");
  const [message, setMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!onSubmit) {
      setMessage("Sign in with the configured identity provider to register a patient.");
      return;
    }

    const values = new FormData(event.currentTarget);
    const request: CreatePatientRequest = {
      legalGivenName: String(values.get("legalGivenName") ?? ""),
      legalFamilyName: String(values.get("legalFamilyName") ?? ""),
      dateOfBirth: String(values.get("dateOfBirth") ?? ""),
      enrollmentDate: String(values.get("enrollmentDate") ?? ""),
      recordStatus: status,
      membershipStatus: String(values.get("membershipStatus") ?? "") || undefined,
      responsibleClinicianUserId:
        String(values.get("responsibleClinicianUserId") ?? "") || undefined,
      careTeamId: String(values.get("careTeamId") ?? "") || undefined,
    };

    setIsSubmitting(true);
    setMessage(null);
    try {
      const patient = await onSubmit(request);
      setMessage(`Patient registered with internal ID ${patient.internalId}.`);
      event.currentTarget.reset();
      setStatus("ADMINISTRATIVE_INTAKE");
    } catch {
      setMessage("Registration failed. Check your access and try again.");
    } finally {
      setIsSubmitting(false);
    }
  }

  const activeRequiresAssignment = status === "ACTIVE";

  return (
    <form onSubmit={submit} className="registration-form">
      <fieldset disabled={isSubmitting}>
        <legend>Patient identity and enrollment</legend>
        <div className="form-grid">
          <label>
            Legal given name
            <input name="legalGivenName" required autoComplete="given-name" />
          </label>
          <label>
            Legal family name
            <input name="legalFamilyName" required autoComplete="family-name" />
          </label>
          <label>
            Date of birth
            <input name="dateOfBirth" type="date" required />
          </label>
          <label>
            Enrollment date
            <input name="enrollmentDate" type="date" required />
          </label>
          <label>
            Record status
            <select
              value={status}
              onChange={(event) =>
                setStatus(event.target.value as PatientRecordStatus)
              }
            >
              {statuses.map((option) => (
                <option key={option} value={option}>
                  {option === "ACTIVE" ? "Active" : "Administrative intake"}
                </option>
              ))}
            </select>
          </label>
          <label>
            Membership status
            <input name="membershipStatus" />
          </label>
          <label>
            Responsible clinician ID
            <input
              name="responsibleClinicianUserId"
              required={activeRequiresAssignment}
              aria-describedby="assignment-help"
            />
          </label>
          <label>
            Care team
            <select
              name="careTeamId"
              required={activeRequiresAssignment}
              defaultValue=""
            >
              <option value="">Select an authorized care team</option>
              {careTeams.map((careTeam) => (
                <option key={careTeam.id} value={careTeam.id}>
                  {careTeam.name}
                </option>
              ))}
            </select>
          </label>
        </div>
        <p id="assignment-help" className="help-text">
          Active patients require a responsible clinician and authorized care
          team. The system generates the internal patient ID.
        </p>
        <button type="submit" disabled={!isAuthenticated || isSubmitting}>
          {isSubmitting ? "Registering…" : "Register patient"}
        </button>
      </fieldset>
      {!isAuthenticated && (
        <p className="notice" role="status">
          Sign-in is required before registration can be submitted.
        </p>
      )}
      {message && <p className="notice" role="status">{message}</p>}
    </form>
  );
}
