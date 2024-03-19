package com.phildev.pmb.service;

import com.phildev.pmb.model.User;
import com.phildev.pmb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
