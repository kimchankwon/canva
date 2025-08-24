package com.interview.canva.dto;

public record GetUserResponse(
    Long id,
    String firstName,
    String lastName,
    String email
) {}
