package com.smartdelivery.authservice.service;

import com.smartdelivery.authservice.dto.AuthenticationResponse;
import com.smartdelivery.authservice.dto.LoginRequest;
import com.smartdelivery.authservice.dto.RegisterRequest;
import com.smartdelivery.authservice.entity.User;
import com.smartdelivery.authservice.mapper.AuthMapper;
import com.smartdelivery.authservice.repository.UserRepository;
import com.smartdelivery.authservice.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User userEntity = authMapper.registerRequestToUser(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntity);

        UserDetails userDetails = authMapper.mapToUserDetails(userEntity);
        String token = jwtUtil.generateToken(userDetails, Collections.singletonList(userEntity.getRole()));
        return authMapper.tokenToAuthenticationResponse(token);
    }


    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = authMapper.mapToUserDetails(userEntity);
        String token = jwtUtil.generateToken(userDetails, Collections.singletonList(userEntity.getRole()));
        return authMapper.tokenToAuthenticationResponse(token);
    }

}
