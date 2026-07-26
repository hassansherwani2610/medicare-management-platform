package com.hassansherwani.medicare.modules.auth.serviceimpl;

import com.hassansherwani.medicare.modules.auth.dto.request.LoginRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RefreshTokenRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RegisterRequest;
import com.hassansherwani.medicare.modules.auth.dto.response.AuthResponse;
import com.hassansherwani.medicare.modules.auth.repository.RefreshTokenRepository;
import com.hassansherwani.medicare.modules.auth.repository.RoleRepository;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.auth.service.AuthService;
import com.hassansherwani.medicare.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        return null;
    }

    @Override
    public void logout(String email) {

    }
}
