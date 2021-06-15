package com.example.library.domain.dto;

import com.example.library.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    @NotEmpty(message = "FirstName should not be empty")
    private String firstName;
    @NotEmpty(message = "LastName should not be empty")
    private String lastName;
    @NotEmpty(message = "MiddleName should not be empty")
    private String middleName;

    private UserType userType;

    @Email(message = "Email should be valid")
    private String email;
}
