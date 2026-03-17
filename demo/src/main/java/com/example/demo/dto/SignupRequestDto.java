package com.example.demo.dto;

public record SignupRequestDto(
        String username,
        String password,
        String nickname
) {}
