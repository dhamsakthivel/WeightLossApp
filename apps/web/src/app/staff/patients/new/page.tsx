import { PatientRegistrationForm } from "@/components/patient-registration-form";

export default function NewPatientPage() {
  return (
    <main>
      <p>Staff workspace</p>
      <h1>Register patient</h1>
      <p>
        Create a longitudinal monitoring record. Patient registration is
        audited and requires authorized care-team access.
      </p>
      <PatientRegistrationForm isAuthenticated={false} careTeams={[]} />
    </main>
  );
}
