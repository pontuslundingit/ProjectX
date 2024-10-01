package com.example.projectx.services;

import com.example.projectx.models.User;
import com.example.projectx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUserIfNotExists(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            return userRepository.save(newUser);
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
