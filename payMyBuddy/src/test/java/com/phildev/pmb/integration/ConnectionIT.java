package com.phildev.pmb.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConnectionIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testee.money@test.fr")
    public void testConnectionPageRenderingWithNoConnection() throws Exception {
        mockMvc.perform(get("/connection"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You have currently no connection")));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void testConnectionPageRenderingWithOneConnection() throws Exception {
        mockMvc.perform(get("/connection"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<td>testee.money@test.fr</td>")))
                .andExpect(content().string(containsString("<td>Testee Money</td>")));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void shouldReturnErrorPageForOwnEmail() throws Exception {
        mockMvc.perform(get("/connection/search?email=test.pmb@test.fr"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You cannot add a connection with your own address")))
                .andExpect(content().string(containsString("<td>Testee Money</td>")));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void shouldReturnErrorPageForUnknownEmail() throws Exception {
        mockMvc.perform(get("/connection/search?email=test@unknown.fr"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Unknown or empty email address")))
                .andExpect(content().string(containsString("<td>Testee Money</td>")));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void shouldReturnErrorPageForEmptyEmail() throws Exception {
        mockMvc.perform(get("/connection/search?email="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Unknown or empty email address")))
                .andExpect(content().string(containsString("<td>Testee Money</td>")));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void shouldReturnAMessageToAddUser() throws Exception {
        mockMvc.perform(get("/connection/search?email=tester.cousy@test.fr"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Do you want to add a connection with Tester Cousy?")))
                .andExpect(content().string(containsString(" <button type=\"submit\" class=\"btn btn-primary col-4 mx-auto shadow font-weight-bold\">Yes</button>")));
    }
}
