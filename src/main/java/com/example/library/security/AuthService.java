package com.example.library.security;

import com.example.library.domain.dto.SignUpDto;
import com.example.library.domain.dto.TokenRequest;
import com.example.library.domain.entity.User;
import com.example.library.domain.enums.UserType;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public String generateToken(TokenRequest tokenRequest) {
        User user = userRepository.findByEmail(tokenRequest.getUsername()).orElseThrow(() -> new RuntimeException());
        if (!passwordEncoder.matches(tokenRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException();
        }
        return tokenService.generateToken(user);
    }

    public String signUpReader(SignUpDto signUpDto) {
        Optional<User> admin = userRepository.findByEmail(signUpDto.getEmail());
        String token = null;
        if (admin.isEmpty()) {
            User user = new User();
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setUserType(UserType.ROLE_READER);
            userRepository.save(user);
            try {
                token = generateToken(
                        TokenRequest.builder()
                                .username(signUpDto.getEmail())
                                .password(signUpDto.getPassword())
                                .build()
                );
            } catch (RuntimeException exception) {
                System.out.println("Не удалось сгенерировать токен пользователя " + signUpDto.getEmail());
                return token;
            }
        }
        return token;
    }

    public String signUpAdmin(SignUpDto signUpDto) {
        Optional<User> admin = userRepository.findByEmail(signUpDto.getEmail());
        String token = null;
        if (admin.isEmpty())
        {
            User user = new User();
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setUserType(UserType.ROLE_ADMIN);
            userRepository.save(user);
        }

        return token;
    }
}
