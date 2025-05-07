package com.ing.store.controller;

import com.ing.store.dto.AuthenticationRequest;
import com.ing.store.dto.AuthenticationResponse;
import com.ing.store.dto.RegisterRequest;
import com.ing.store.dto.UserDto;
import com.ing.store.service.AuthenticationService;
import com.ing.store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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

    @GetMapping
    public UserDto getCurrentUser() {
        try {
            return userService.getCurrentUserDto();
        } catch (Exception e) {
            throw new IllegalStateException("User not authenticated");
        }
    }
}

