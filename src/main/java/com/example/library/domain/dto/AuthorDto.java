package com.example.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private UUID id;

    @NotEmpty(message = "FirstName should not be empty")
    private String firstName;
    @NotEmpty(message = "LastName should not be empty")
    private String lastName;
    @NotEmpty(message = "MiddleName should not be empty")
    private String middleName;
}
