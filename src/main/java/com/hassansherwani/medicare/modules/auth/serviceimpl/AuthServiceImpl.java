package com.hassansherwani.medicare.modules.auth.serviceimpl;

import com.hassansherwani.medicare.common.exception.DuplicateResourceException;
import com.hassansherwani.medicare.common.exception.ResourceNotFoundException;
import com.hassansherwani.medicare.modules.auth.dto.request.LoginRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RefreshTokenRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RegisterRequest;
import com.hassansherwani.medicare.modules.auth.dto.response.AuthResponse;
import com.hassansherwani.medicare.modules.auth.dto.response.UserResponse;
import com.hassansherwani.medicare.modules.auth.entity.RefreshToken;
import com.hassansherwani.medicare.modules.auth.entity.Role;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.auth.repository.RefreshTokenRepository;
import com.hassansherwani.medicare.modules.auth.repository.RoleRepository;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.auth.service.AuthService;
import com.hassansherwani.medicare.security.CustomUserDetailsService;
import com.hassansherwani.medicare.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    // To send response in DTO form not in ENTITY form
    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }

    // To login and sending responses
    private AuthResponse authenticateAndBuildResponse(String email, String rawPassword){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found of email: " + email));

        String accessTokenValue = jwtTokenProvider.generateAccessToken(authentication);
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder()
                        .user(user)
                        .build());

        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000)); // Refresh Token valid till 7 days
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    // 1. To register or signup new user
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw  new DuplicateResourceException("User is already registered with email: " + request.getEmail());
        }

        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found of: " + request.getRole()));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(roles)
                .build();

        userRepository.save(user);

        return authenticateAndBuildResponse(request.getEmail(), request.getPassword());
    }

    // 2. To Login
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        return authenticateAndBuildResponse(request.getEmail(), request.getPassword());
    }

    // 3. To generate new access token with the help of refresh token
    @Override
    @Transactional
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (storedRefreshToken.getRevoked() || storedRefreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new ResourceNotFoundException("Refresh token expired or revoked. Please log in again.");
        }

        User user = storedRefreshToken.getUser();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String newAccessTokenValue = jwtTokenProvider.generateAccessToken(authentication);

        return AuthResponse.builder()
                .accessToken(newAccessTokenValue)
                .refreshToken(request.getRefreshToken()) // Same value which comes in "request"
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    // 4. To logout and delete refresh token
    @Override
    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found of email: " + email));

        refreshTokenRepository.deleteByUser(user);
    }
}
