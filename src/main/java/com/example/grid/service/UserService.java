package com.example.grid.service;

import com.example.grid.data.db.User;
import com.example.grid.data.payload.user.Authenticated;
import com.example.grid.data.payload.user.CreateUser;
import com.example.grid.repository.UserRepository;
import com.example.grid.security.JWTTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider tokenProvider;
    private final AuthenticationManager auth;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTTokenProvider tokenProvider, AuthenticationManager auth) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auth = auth;
    }

    public Authenticated authenticate(String username, String password) {
        Authentication auth = this.auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new Authenticated(this.tokenProvider.create(username));
    }

    public Authenticated create(CreateUser payload) {
        if (this.userRepository.existsById(payload.getUsername())) throw new EntityExistsException();
        User user = new User();
        user.setUsername(payload.getUsername());
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        this.userRepository.save(user);
        return new Authenticated(this.tokenProvider.create(user.getUsername()));
    }
}
