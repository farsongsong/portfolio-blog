package com.example.demo.dto;

import com.example.demo.entity.RoleType;
import com.example.demo.entity.User;

public record UserResponseDto(
        Long id,
        String username,
        String nickname,
        RoleType role
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole()
        );
    }
}
