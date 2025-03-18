package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String HARDCODED_USERNAME = "admin";
    private static final String HARDCODED_PASSWORD = "1234";

    public boolean authenticate(String username, String password) {
        // Check against hardcoded credentials
        return HARDCODED_USERNAME.equals(username) && HARDCODED_PASSWORD.equals(password);
    }
}
