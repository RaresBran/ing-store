package com.ing.store.controller;

import com.ing.store.dto.AuthenticationRequest;
import com.ing.store.dto.AuthenticationResponse;
import com.ing.store.dto.RegisterRequest;
import com.ing.store.dto.UserDto;
import com.ing.store.service.AuthenticationService;
import com.ing.store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody @Valid RegisterRequest request) {
        return authenticationService.register(request);
    }

    @GetMapping()
    public UserDto getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication);
    }
}

