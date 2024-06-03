package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public User login(String userID, String userPassword) {
        return userRepository.findByUserIDAndUserPassword(userID, userPassword);
    }
}