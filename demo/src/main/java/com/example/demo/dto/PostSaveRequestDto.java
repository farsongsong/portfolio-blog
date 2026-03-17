package com.example.demo.dto;

public record PostSaveRequestDto(
        String title,
        String content,
        String category
) {}
