package com.example.demo.controller;

import com.example.demo.dto.AuthenticationRequestDto;
import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRest {

    private final AuthenticationService authService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        log.info("[AuthenticationRest] register");
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        log.info("[AuthenticationRest] authenticate");
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserResponseDto> getLoggedInUser() {
        log.info("[AuthenticationRest] getLoggedInUser");
        return ResponseEntity.ok(userService.getLoggedInUser());
    }

}
