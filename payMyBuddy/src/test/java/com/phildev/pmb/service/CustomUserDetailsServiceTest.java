package com.phildev.pmb.service;

import com.phildev.pmb.config.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testUserFoundByEmailWithUserRole(){
        SimpleGrantedAuthority simpleUserGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        SimpleGrantedAuthority simpleAdminGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test.pmb@test.fr");
        Assert.isTrue(userDetails.getUsername().equals("test.pmb@test.fr"), "Email found in Db for user");
        Assert.isTrue(userDetails.getAuthorities().contains(simpleUserGrantedAuthority), "User has role USER");
        Assert.isTrue(!userDetails.getAuthorities().contains(simpleAdminGrantedAuthority), "User has not the role ADMIN");
    }

    @Test
    public void testAdminFoundByEmailWithAdminRole(){
        SimpleGrantedAuthority simpleUserGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        SimpleGrantedAuthority simpleAdminGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("joe.admin@pmb.fr");
        Assert.isTrue(userDetails.getUsername().equals("joe.admin@pmb.fr"), "Email found in Db for user");
        Assert.isTrue(!userDetails.getAuthorities().contains(simpleUserGrantedAuthority), "User has not the role USER");
        Assert.isTrue(userDetails.getAuthorities().contains(simpleAdminGrantedAuthority), "User has  the role ADMIN");
    }

    @Test
    public void testUserNotFoundByEmail() {
        Assertions.assertThrows(UsernameNotFoundException.class, ()-> customUserDetailsService.loadUserByUsername("je.admin@pmb.fr"));
    }
}
