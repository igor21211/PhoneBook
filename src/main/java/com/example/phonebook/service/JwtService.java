package com.example.phonebook.service;

import com.example.phonebook.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String createToken(User user);
    String resolveToken(HttpServletRequest servletRequest);
    boolean validateToken(String token);
    Authentication createAuthentication(String token);
    User extractUser(String token);
}
