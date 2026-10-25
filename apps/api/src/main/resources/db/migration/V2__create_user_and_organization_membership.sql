create table user_accounts (
    id uuid primary key,
    issuer varchar(512) not null,
    subject varchar(512) not null,
    display_name varchar(200),
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uk_user_accounts_issuer_subject unique (issuer, subject),
    constraint chk_user_accounts_status check (status in ('ACTIVE', 'DEACTIVATED'))
);

create table organization_memberships (
    id uuid primary key,
    organization_id uuid not null,
    user_id uuid not null,
    role varchar(32) not null,
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint fk_organization_memberships_organization
        foreign key (organization_id) references organizations(id),
    constraint fk_organization_memberships_user
        foreign key (user_id) references user_accounts(id),
    constraint uk_organization_memberships_organization_user_role
        unique (organization_id, user_id, role),
    constraint chk_organization_memberships_role
        check (role in ('PHYSICIAN', 'MEDICAL_ASSISTANT', 'ADMINISTRATOR', 'PRIVACY_REVIEWER')),
    constraint chk_organization_memberships_status check (status in ('ACTIVE', 'DEACTIVATED'))
);

alter table audit_events
    add constraint fk_audit_events_actor_user
    foreign key (actor_user_id) references user_accounts(id);

create index idx_organization_memberships_user_status
    on organization_memberships (user_id, status);
