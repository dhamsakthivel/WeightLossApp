create table care_teams (
    id uuid primary key,
    organization_id uuid not null,
    name varchar(200) not null,
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_care_teams_organization
        foreign key (organization_id) references organizations(id),
    constraint uk_care_teams_organization_name unique (organization_id, name),
    constraint chk_care_teams_status check (status in ('ACTIVE', 'INACTIVE'))
);

create table care_team_memberships (
    id uuid primary key,
    care_team_id uuid not null,
    user_id uuid not null,
    status varchar(32) not null,
    starts_at timestamp with time zone not null,
    ends_at timestamp with time zone,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_care_team_memberships_care_team
        foreign key (care_team_id) references care_teams(id),
    constraint fk_care_team_memberships_user
        foreign key (user_id) references user_accounts(id),
    constraint uk_care_team_memberships_team_user unique (care_team_id, user_id),
    constraint chk_care_team_memberships_status check (status in ('ACTIVE', 'DEACTIVATED'))
);

create table clinical_access_grants (
    id uuid primary key,
    organization_id uuid not null,
    grantee_user_id uuid not null,
    care_team_id uuid not null,
    approved_by_user_id uuid not null,
    status varchar(32) not null,
    reason varchar(500) not null,
    effective_at timestamp with time zone not null,
    expires_at timestamp with time zone not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_clinical_access_grants_organization
        foreign key (organization_id) references organizations(id),
    constraint fk_clinical_access_grants_grantee
        foreign key (grantee_user_id) references user_accounts(id),
    constraint fk_clinical_access_grants_care_team
        foreign key (care_team_id) references care_teams(id),
    constraint fk_clinical_access_grants_approver
        foreign key (approved_by_user_id) references user_accounts(id),
    constraint chk_clinical_access_grants_status check (status in ('ACTIVE', 'REVOKED', 'EXPIRED')),
    constraint chk_clinical_access_grants_window check (expires_at > effective_at)
);

create index idx_care_team_memberships_user_status
    on care_team_memberships (user_id, status);

create index idx_clinical_access_grants_grantee_window
    on clinical_access_grants (grantee_user_id, status, effective_at, expires_at);
