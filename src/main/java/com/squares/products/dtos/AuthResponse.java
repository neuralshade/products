package com.squares.products.dtos;

public record AuthResponse(String token, String type, String username, String role) {
}
