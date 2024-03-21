package com.phildev.pmb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
@ProfileValueSourceConfiguration
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void userLoginTestShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("test.pmb@test.fr").password("Test2024@")).andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles ={"ADMIN"}, username = "admin@test.fr", password = "admin")
    public void adminUserShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("admin@test.fr").password("admin")).andExpect(authenticated());
    }

    @Test
    public void realAdminUserShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("joe.admin@pmb.fr").password("Admin2024@")).andExpect(authenticated());
    }


    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("test.p@test.fr").password("Test20211@")).andExpect(unauthenticated());
    }


}
