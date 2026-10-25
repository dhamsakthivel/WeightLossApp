package com.weightloss.api.foundation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.weightloss.api.foundation.security.ApiSecurityConfiguration;

@WebMvcTest(SystemStatusController.class)
@Import(ApiSecurityConfiguration.class)
class SystemStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsANonCacheableNonPhiAvailabilityResponse() throws Exception {
        mockMvc.perform(get("/api/v1/system/status"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.service").value("weight-management-api"))
                .andExpect(jsonPath("$.status").value("available"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void deniesRoutesThatHaveNotBeenExplicitlyAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isForbidden());
    }
}
