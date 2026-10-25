package com.weightloss.api.patient;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByOrganizationIdAndInternalId(UUID organizationId, String internalId);
}
