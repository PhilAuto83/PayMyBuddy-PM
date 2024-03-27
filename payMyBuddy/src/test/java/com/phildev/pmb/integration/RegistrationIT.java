package com.phildev.pmb.integration;

import com.phildev.pmb.model.Account;
import com.phildev.pmb.model.User;
import com.phildev.pmb.repository.AccountRepository;
import com.phildev.pmb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;



    @Test
    public void testUserCreationThroughRegistrationForm() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName","Joey")
                .param("lastName", "Tester")
                .param("email","joey.test@test.fr")
                .param("password", "Test0000@"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Registration Success</title>")))
                .andExpect(content().string(containsString("Your account has been created successfully.")));
        User joey = userRepository.findByEmail("joey.test@test.fr");
        Optional<Account> joeyAccount = accountRepository.findById(joey.getId());
        Assertions.assertEquals("USER", joey.getRole());
        Assertions.assertEquals("joey.test@test.fr", joey.getEmail());
        Assertions.assertNotNull(joey.getPassword());
        Assertions.assertNotNull(joey.getPassword());
        Assertions.assertEquals(joey.getId(),joeyAccount.get().getId());
    }

    @ParameterizedTest(name=" Check invalid values {0}, {1}, {2}, {3} does not create an account.")
    @CsvSource({"Test, T2, test@gmail.com, Test@2222", "Test, te, test@gmail.com, Test@2222", "Test, eT, test@gmail.com, Test@2222"})
    public void testWrongFirstNameValidationThroughRegistrationForm(String firstName, String lastName, String email, String password) throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Registration Form</title>")))
                .andExpect(content().string(containsString("[Lastname should start with capital letter followed by lowercase letter(s)]")));

        User joey = userRepository.findByEmail(email);
        Assertions.assertNull(joey);

    }

    @ParameterizedTest(name=" Check invalid values {0}, {1}, {2}, {3} does not create an account.")
    @CsvSource({"Test, Test, test@gmail.com, test@2222", "Test, Test, test@gmail.com, Test/2222", "Test, Test, test@gmail.com, T@2222"})
    public void testWrongPasswordValidationThroughRegistrationForm(String firstName, String lastName, String email, String password) throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Registration Form</title>")))
                .andExpect(content().string(containsString("Password should contain at least 1 capital letter, 1 lowercase letter, 1 digit,\n" +
                        "1 special character between @$!%*#?&")))
                .andExpect(content().string(containsString("minimum 8 characters")));

        User joey = userRepository.findByEmail("joey.test@test.fr");
        Assertions.assertNull(joey);
    }

    @Test
    public void testWrongFirstNameValidationThroughRegistrationForm() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "joey")
                        .param("lastName", "Tester")
                        .param("email", "joey.test@test.fr")
                        .param("password", "Test0000@"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Registration Form</title>")))
                .andExpect(content().string(containsString("[Firstname should start with capital letter followed by lowercase letter(s)]")));

        User joey = userRepository.findByEmail("joey.test@test.fr");
        Assertions.assertNull(joey);
    }

}
