package com.example.library.services;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Validated
public interface UserService {
    Optional<UserDto> create(UserDto personDto);
    Optional<UserDto> findById(UUID id);
    void delete(UUID id);
    Optional<UserDto> updateByPut(UserDto userDto, UUID id);
    Set<UserDto> findByFirstLastMiddleName(String firstName, String lastName, String middleName);
    Optional<UserDto> findByEmail(String email);
    Page<UserDto> getAll(Pageable pageable);
    Optional<UserDto> assignUserToBook(UUID idUser, UUID idBook);
    Optional<UserDto> unassignUserToBook(UUID idUser, UUID idBook);
    Optional<BookDto> assignBookToUser(UUID idBook, UUID idUser);
    Optional<BookDto> unassignBookToUser(UUID idBook, UUID idUser);
}
