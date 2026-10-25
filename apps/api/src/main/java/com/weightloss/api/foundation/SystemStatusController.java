package com.weightloss.api.foundation;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Non-PHI endpoint used to verify that the API is reachable.
 *
 * Authorization is deliberately not introduced until the OIDC foundation is
 * implemented. No patient or organization data is available from this endpoint.
 */
@RestController
@RequestMapping("/api/v1/system")
public class SystemStatusController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(Map.of(
                        "service", "weight-management-api",
                        "status", "available",
                        "timestamp", Instant.now().toString()));
    }
}
