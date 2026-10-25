create table organizations (
    id uuid primary key,
    slug varchar(63) not null unique,
    display_name varchar(200) not null,
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint chk_organizations_status check (status in ('ACTIVE', 'SUSPENDED'))
);

create table audit_events (
    id uuid primary key,
    organization_id uuid,
    event_type varchar(100) not null,
    actor_user_id uuid,
    resource_type varchar(100) not null,
    resource_id uuid,
    outcome varchar(32) not null,
    occurred_at timestamp with time zone not null,
    correlation_id uuid,
    details_json text not null,
    constraint fk_audit_events_organization
        foreign key (organization_id) references organizations(id),
    constraint chk_audit_events_outcome check (outcome in ('SUCCESS', 'DENIED', 'FAILURE'))
);

create index idx_audit_events_organization_occurred_at
    on audit_events (organization_id, occurred_at);
