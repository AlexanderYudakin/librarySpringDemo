package com.example.library.services;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Validated
public interface AuthorService {
    Optional<AuthorDto> create(AuthorDto authorDto);
    Optional<AuthorDto> findById(UUID id);
    void delete(UUID id);
    Optional<AuthorDto> updateByPut(AuthorDto authorDto, UUID id);
    Set<AuthorDto> findByFirstLastMiddleName(String firstName, String lastName, String middleName);
    Page<AuthorDto> getAll(Pageable pageable);
    Optional<AuthorDto> assignAuthorToBook(UUID idAuthor, UUID idBook);
    Optional<AuthorDto> unassignAuthorToBook(UUID idAuthor, UUID idBook);
    Optional<BookDto> assignBookToAuthor(UUID idBook, UUID idAuthor);
    Optional<BookDto> unassignBookToAuthor(UUID idAuthor, UUID idBook);
}
