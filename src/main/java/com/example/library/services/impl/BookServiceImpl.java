package com.example.library.services.impl;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ConversionService conversionService;

    @Override
    public Optional<BookDto> create(BookDto bookDto) {
        Book book = conversionService.convert(bookDto, Book.class);
        bookRepository.save(book);
        return Optional.of(conversionService.convert(book, BookDto.class));
    }

    @Override
    public Optional<BookDto> findById(UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> conversionService.convert(value, BookDto.class));
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new InvalidParameterException();
        }
        Optional<Book> deletedBook = bookRepository.findById(id);
        deletedBook.ifPresent(bookRepository::delete);
    }

    @Override
    public Optional<BookDto> updateByPut(BookDto bookDto, UUID id) {
        Book book = conversionService.convert(bookDto, Book.class);
        book.setId(id);
        bookRepository.save(book);
        return Optional.of(conversionService.convert(book, BookDto.class));
    }

    @Override
    public Set<BookDto> getByTitle(String title) {
        return bookRepository.findByTitle(title).stream()
                .map(book -> conversionService.convert(book, BookDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> conversionService.convert(book, BookDto.class));
    }
}
