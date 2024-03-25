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
public class TransactionIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void testRenderingTransactionPageWithOidcUser() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<option value=\"1\">Testee Money</option>")))
                .andExpect(content().string(containsString("<td>Movie tickets</td>")))
                .andExpect(content().string(containsString("<td>15.0</td>")));
    }

}
