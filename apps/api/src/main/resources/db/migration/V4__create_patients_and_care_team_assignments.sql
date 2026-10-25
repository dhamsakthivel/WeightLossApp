create table patients (
    id uuid primary key,
    organization_id uuid not null,
    internal_id varchar(64) not null,
    legal_given_name varchar(100) not null,
    legal_family_name varchar(100) not null,
    date_of_birth date not null,
    enrollment_date date not null,
    record_status varchar(32) not null,
    membership_status varchar(32),
    responsible_clinician_user_id uuid,
    status_reason varchar(500),
    status_effective_at timestamp with time zone not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_patients_organization
        foreign key (organization_id) references organizations(id),
    constraint fk_patients_responsible_clinician
        foreign key (responsible_clinician_user_id) references user_accounts(id),
    constraint uk_patients_organization_internal_id unique (organization_id, internal_id),
    constraint chk_patients_record_status
        check (record_status in ('ADMINISTRATIVE_INTAKE', 'ACTIVE', 'INACTIVE', 'DISCHARGED')),
    constraint chk_patients_active_requires_clinician
        check (record_status <> 'ACTIVE' or responsible_clinician_user_id is not null)
);

create table patient_care_team_assignments (
    id uuid primary key,
    patient_id uuid not null,
    care_team_id uuid not null,
    status varchar(32) not null,
    starts_at timestamp with time zone not null,
    ends_at timestamp with time zone,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_patient_care_team_assignments_patient
        foreign key (patient_id) references patients(id),
    constraint fk_patient_care_team_assignments_care_team
        foreign key (care_team_id) references care_teams(id),
    constraint uk_patient_care_team_assignments_patient_team unique (patient_id, care_team_id),
    constraint chk_patient_care_team_assignments_status check (status in ('ACTIVE', 'INACTIVE'))
);

create index idx_patients_organization_record_status
    on patients (organization_id, record_status);

create index idx_patient_care_team_assignments_patient_status
    on patient_care_team_assignments (patient_id, status);
