package com.example.library.config;

import com.example.library.domain.dto.SignUpDto;
import com.example.library.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminInit {
    private final AuthService authService;

    @PostConstruct
    private void postConstruct() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("admin@admin");
        signUpDto.setPassword("admin");
        authService.signUpAdmin(signUpDto);
    }
}
