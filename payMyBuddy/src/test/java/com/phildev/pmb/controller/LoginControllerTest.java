package com.phildev.pmb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void userLoginTestShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("phil.pmb@test.fr").password("Test2024@")).andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles ={"ADMIN"}, username = "admin@test.fr", password = "admin")
    public void adminUserShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("admin@test.fr").password("admin")).andExpect(authenticated());
    }


    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("phil.p@test.fr").password("Test20211@")).andExpect(unauthenticated());
    }


}
