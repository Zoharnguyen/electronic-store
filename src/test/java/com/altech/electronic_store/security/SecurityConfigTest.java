package com.altech.electronic_store.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenUnauthenticated_thenReturn401() throws Exception {
        mockMvc.perform(get("/client-api/v1/products"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenUserAuthenticated_thenDenyProductAccess() throws Exception {
        mockMvc.perform(get("/client-api/v1/products"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void whenUserAuthenticated_thenAllowBasketAccess() throws Exception {
        mockMvc.perform(post("/client-api/v1/baskets"))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenUserAuthenticated_thenDenyAdminAccess() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenAdminAuthenticated_thenAllowAll() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }
} 