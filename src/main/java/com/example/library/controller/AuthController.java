package com.example.library.controller;

import com.example.library.domain.dto.SignUpDto;
import com.example.library.domain.dto.TokenRequest;
import com.example.library.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public String getToken(@RequestBody TokenRequest tokenRequest) {
        return authService.generateToken(tokenRequest);
    }

    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignUpDto signUpDto) {
        return authService.signUpReader(signUpDto);
    }
}
