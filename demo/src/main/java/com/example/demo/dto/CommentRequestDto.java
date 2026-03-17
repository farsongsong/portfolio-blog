package com.example.demo.dto;

public record CommentRequestDto(
        Long postId,
        String content
) {}
