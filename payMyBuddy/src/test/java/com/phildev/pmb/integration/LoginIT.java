package com.phildev.pmb.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ProfileValueSourceConfiguration
public class LoginIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void userLoginTestShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("test.pmb@test.fr").password("Test2024@"))
                .andDo(print())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void adminUserShouldReturn200() throws Exception {
        mockMvc.perform(formLogin("/login").user("joe.admin@pmb.fr").password("Admin2024@"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("test.p@test.fr").password("Test20211@"))
                .andDo(print())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void userLandOnHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Home</title>")))
                 .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Welcome Test,</h2>")));
            }

    @Test
    @WithMockUser(username = "test.pmb@test.fr")
    public void unauthorizedUserForAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "joe.admin@pmb.fr", roles="ADMIN")
    public void authorizedUserForAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Admin Home</title>")))
                .andExpect(content().string(containsString("Welcome Joe,</h2>")));
    }

    @Test
    public void checkErrorMessageWithOidcUserWithEmail() throws Exception {
        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                OidcIdToken.withTokenValue("id-token")
                        .claim("user_name", "Johnny")
                        .claim("email", "johnny@test.fr").build(),
                "user_name");
        mockMvc.perform(get("/login-check")
                        .with(oidcLogin().oidcUser(oidcUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<div class=\"alert alert-danger\">The email johnny@test.fr is not known from Pay My Buddy</div>")));
    }

    @Test
    public void checkErrorMessageWithOidcUserWithoutEmail() throws Exception {
        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                OidcIdToken.withTokenValue("id-token")
                        .claim("user_name", "johnny@test.fr").build(),
                "user_name");
        mockMvc.perform(get("/login-check")
                        .with(oidcLogin().oidcUser(oidcUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<div class=\"alert alert-danger\">User is not known from Pay My Buddy</div>")));
    }

    @Test
    public void checkHomePageLandingWithOidcKnownUser() throws Exception {
        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                OidcIdToken.withTokenValue("id-token")
                        .claim("user_name","Test")
                        .claim("given_name","Test")
                        .claim("email", "test.pmb@test.fr").build(),
                "user_name");
        mockMvc.perform(get("/login-check")
                        .with(oidcLogin().oidcUser(oidcUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Test,</h2>")))
        .andExpect(content().string(containsString("You can now start moving money around with your friends. Enjoy your stay in Pay My Buddy and share it around you.<p>")));
    }


}
