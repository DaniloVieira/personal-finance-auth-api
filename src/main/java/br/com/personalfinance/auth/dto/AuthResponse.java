package br.com.personalfinance.auth.dto;

public record AuthResponse(
        String token,
        String name,
        String email
) {}
