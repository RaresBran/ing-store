package com.ing.store.service;

import com.ing.store.config.security.JwtService;
import com.ing.store.dto.AuthenticationRequest;
import com.ing.store.dto.AuthenticationResponse;
import com.ing.store.dto.RegisterRequest;
import com.ing.store.exception.AuthenticationException;
import com.ing.store.model.Role;
import com.ing.store.model.User;
import com.ing.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException(e.getMessage());
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(AuthenticationException::new);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request)  {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        try {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(savedUser);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (ConstraintViolationException e) {
            throw new AuthenticationException("Email already in use");
        }

    }
}
