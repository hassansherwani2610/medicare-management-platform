package com.hassansherwani.medicare.modules.auth.serviceimpl;

import com.hassansherwani.medicare.common.exception.DuplicateResourceException;
import com.hassansherwani.medicare.common.exception.ResourceNotFoundException;
import com.hassansherwani.medicare.modules.auth.dto.request.LoginRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RefreshTokenRequest;
import com.hassansherwani.medicare.modules.auth.dto.request.RegisterRequest;
import com.hassansherwani.medicare.modules.auth.dto.response.AuthResponse;
import com.hassansherwani.medicare.modules.auth.dto.response.UserResponse;
import com.hassansherwani.medicare.modules.auth.entity.Role;
import com.hassansherwani.medicare.modules.auth.entity.User;
import com.hassansherwani.medicare.modules.auth.repository.RefreshTokenRepository;
import com.hassansherwani.medicare.modules.auth.repository.RoleRepository;
import com.hassansherwani.medicare.modules.auth.repository.UserRepository;
import com.hassansherwani.medicare.modules.auth.service.AuthService;
import com.hassansherwani.medicare.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    // To send response in DTO not an ENTITY
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


        return null;
    }

    // 1. To register or signup new user
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw  new DuplicateResourceException("User with this email is already registered: " + request.getEmail());
        }

        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

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
        return null;
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
