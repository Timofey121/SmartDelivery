package com.smartdelivery.authservice.mapper;

import com.smartdelivery.authservice.dto.AuthenticationResponse;
import com.smartdelivery.authservice.dto.RegisterRequest;
import com.smartdelivery.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = java.util.Locale.class)
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "role", expression = "java(\"ROLE_\" + request.getRole().toUpperCase(Locale.ROOT))")
    User registerRequestToUser(RegisterRequest request);

    default AuthenticationResponse tokenToAuthenticationResponse(String token) {
        return new AuthenticationResponse(token);
    }

    default SimpleGrantedAuthority roleToAuthority(String role) {
        return new SimpleGrantedAuthority(role);
    }

    default UserDetails mapToUserDetails(User userEntity) {
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.singletonList(roleToAuthority(userEntity.getRole()))
        );
    }

}
