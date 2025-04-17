package com.timofey.userservice.controller;

import com.timofey.userservice.dto.UserResponse;
import com.timofey.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    @GetMapping("/couriers")
    public List<UserResponse> getCouriers() {
        return userService.getAllCouriers();
    }
}
