package com.weightloss.api.patient.api;

import java.util.UUID;

import com.weightloss.api.patient.Patient;
import com.weightloss.api.patient.PatientRecordStatus;

public record PatientRegistrationResponse(UUID id, String internalId, PatientRecordStatus recordStatus) {

    static PatientRegistrationResponse from(Patient patient) {
        return new PatientRegistrationResponse(patient.getId(), patient.getInternalId(), patient.getRecordStatus());
    }
}
