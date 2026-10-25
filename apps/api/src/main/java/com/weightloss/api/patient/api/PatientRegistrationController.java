package com.weightloss.api.patient.api;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weightloss.api.foundation.identity.AuthenticatedUserResolver;
import com.weightloss.api.patient.CreatePatientCommand;
import com.weightloss.api.patient.Patient;
import com.weightloss.api.patient.PatientRegistrationAuthorization;
import com.weightloss.api.patient.PatientRegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/patients")
public class PatientRegistrationController {

    private final AuthenticatedUserResolver authenticatedUsers;
    private final PatientRegistrationAuthorization authorization;
    private final PatientRegistrationService registrations;

    public PatientRegistrationController(AuthenticatedUserResolver authenticatedUsers,
            PatientRegistrationAuthorization authorization,
            PatientRegistrationService registrations) {
        this.authenticatedUsers = authenticatedUsers;
        this.authorization = authorization;
        this.registrations = registrations;
    }

    @PostMapping
    public ResponseEntity<PatientRegistrationResponse> register(
            @PathVariable UUID organizationId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreatePatientRequest request) {
        UUID actorUserId = authenticatedUsers.resolveActiveUserId(jwt);
        CreatePatientCommand command = new CreatePatientCommand(
                request.legalGivenName(),
                request.legalFamilyName(),
                request.dateOfBirth(),
                request.enrollmentDate(),
                request.recordStatus(),
                request.membershipStatus(),
                request.responsibleClinicianUserId(),
                request.careTeamId());

        authorization.requireRegistrationAccess(actorUserId, organizationId, command);
        Patient patient = registrations.register(organizationId, actorUserId, command);
        PatientRegistrationResponse response = PatientRegistrationResponse.from(patient);

        return ResponseEntity.created(
                URI.create("/api/v1/organizations/" + organizationId + "/patients/" + patient.getId()))
                .body(response);
    }
}
