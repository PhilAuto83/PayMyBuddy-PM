package com.phildev.pmb.service;

import com.phildev.pmb.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;



    @Test
    public void testUserFoundByEmail(){
        User user = userService.findUserByEmail("test.pmb@test.fr");
        Assert.isTrue(user.getEmail().equals("test.pmb@test.fr"), "user email is retrieved successfully");
        Assert.isTrue(user.getFirstName().equals("Test"), "user firstname is retrieved successfully");
        Assert.isTrue(user.getLastName().equals("Pmb"), "user lastname is retrieved successfully");
        Assert.isTrue(user.getRole().equals("USER"), "user role is User");
    }

    @Test
    public void testAdminFoundByEmail(){
        User user = userService.findUserByEmail("joe.admin@pmb.fr");
        Assert.isTrue(user.getEmail().equals("joe.admin@pmb.fr"), "user email is retrieved successfully");
        Assert.isTrue(user.getFirstName().equals("Joe"), "user firstname is retrieved successfully");
        Assert.isTrue(user.getLastName().equals("Admin"), "user lastname is retrieved successfully");
        Assert.isTrue(user.getRole().equals("ADMIN"), "user role is User");
    }

    @Test
    public void testNoUserFoundByEmail(){
        User user = userService.findUserByEmail("je.admin@pmb.fr");
        Assert.isNull(user, "no user found");

    }
}
