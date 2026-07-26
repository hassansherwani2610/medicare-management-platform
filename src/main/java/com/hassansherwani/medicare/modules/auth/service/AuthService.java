package com.hassansherwani.medicare.modules.auth.service;

import com.hassansherwani.medicare.modules.auth.dto.request.LoginRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RefreshTokenRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RegisterRequest;
import com.hassansherwani.medicare.modules.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshAccessToken(RefreshTokenRequest request);
    void logout(String email);
}
