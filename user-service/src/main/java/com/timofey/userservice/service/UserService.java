package com.timofey.userservice.service;

import com.timofey.userservice.dto.UserResponse;
import com.timofey.userservice.entity.User;
import com.timofey.userservice.mapper.UserMapper;
import com.timofey.userservice.repository.UserRepository;
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
        return userMapper.UserToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::UserToUserResponse).toList();
    }
}
