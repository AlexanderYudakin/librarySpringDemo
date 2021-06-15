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
public interface BookService {
    Optional<BookDto> create(BookDto bookDto);
    Optional<BookDto> findById(UUID id);
    void delete(UUID id);
    Optional<BookDto> updateByPut(BookDto bookDto, UUID id);
    Set<BookDto> getByTitle(String title);
    Page<BookDto> getAll(Pageable pageable);
}
