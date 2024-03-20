package com.phildev.pmb.service;

import com.phildev.pmb.model.User;
import com.phildev.pmb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User save(User user){
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$");
        Matcher matcher = pattern.matcher(user.getPassword());
        Iterable<User> usersFoundInDb =userRepository.findAll();
        for (User userInDb : usersFoundInDb){
            if(userInDb.getFirstName().equals(user.getFirstName())
            && userInDb.getLastName().equals(user.getLastName())){
                logger.error("User {} already exists in database", user.getFirstName()+" "+user.getLastName());
                throw new RuntimeException(String.format("User %s already exists", user.getFirstName()+" "+user.getLastName()));
            }
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            logger.error("User already exists with email : {}", user.getEmail());
            throw new RuntimeException("User already exists with email : " + user.getEmail());
        }else if(!matcher.find()){
            throw new RuntimeException("""                 
                    Password should contain at least 1 capital letter, 1 lowercase letter, 1 digit,
                    1 special character between @$!%*#?&, minimum 8 characters"""
                    );
        }else{
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return userRepository.save(user);
        }

    }
}
