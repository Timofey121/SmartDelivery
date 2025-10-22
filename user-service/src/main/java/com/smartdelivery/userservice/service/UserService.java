package com.smartdelivery.userservice.service;

import com.smartdelivery.userservice.dto.UserResponse;
import com.smartdelivery.userservice.entity.User;
import com.smartdelivery.userservice.mapper.UserMapper;
import com.smartdelivery.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.userToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse).toList();
    }
}
