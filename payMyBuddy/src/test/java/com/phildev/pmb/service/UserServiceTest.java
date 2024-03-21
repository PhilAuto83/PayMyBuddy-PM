package com.phildev.pmb.service;

import com.phildev.pmb.model.User;
import com.phildev.pmb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;



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

    @Test
    public void testUserSavedMethod(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Testy");
        user.setEmail("testy@test.fr");
        user.setPassword("Test@6666");
        userService.save(user);
        User userInDb = userRepository.findByEmail("testy@test.fr");
        Assertions.assertEquals(user.getEmail(), userInDb.getEmail());
    }

    @Test
    public void testExceptionForExistingEmail(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Testy");
        user.setEmail("test.pmb@test.fr");
        user.setPassword("Test@6666");
        Assertions.assertThrows(RuntimeException.class, () -> userService.save(user) );
    }

    @Test
    public void testExceptionForExistingUser(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Pmb");
        user.setEmail("tst.pmb@test.fr");
        user.setPassword("Test@6666");
        Assertions.assertThrows(RuntimeException.class, () -> userService.save(user) );
    }

   @Test
    public void testExceptionForPasswordWithoutCapitalLetter(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Testy");
        user.setEmail("testy@test.fr");
        user.setPassword("test@6666");
        Assertions.assertThrows(RuntimeException.class, () -> userService.save(user));
    }

    @Test
    public void testExceptionForPasswordWithoutAuthorizedSpecialCharacter(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Testy");
        user.setEmail("testy@test.fr");
        user.setPassword("Test/6666");
        Assertions.assertThrows(RuntimeException.class, () -> userService.save(user) );
    }
}
