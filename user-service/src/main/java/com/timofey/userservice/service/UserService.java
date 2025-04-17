package com.timofey.userservice.service;

import com.timofey.userservice.dto.UserResponse;
import com.timofey.userservice.entity.User;
import com.timofey.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public List<UserResponse> getAllCouriers() {
        return userRepository.findByRole("ROLE_COURIER").stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole()))
                .toList();
    }
}
