package com.example.library.domain.dto;

import com.example.library.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private UUID id;

    private UserType userType;

    private String email;
    private String password;
}
