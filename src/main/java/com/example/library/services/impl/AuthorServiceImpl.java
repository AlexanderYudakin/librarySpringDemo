package com.example.library.services.impl;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.services.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ConversionService conversionService;
    private final BookService bookService;

    @Override
    public Optional<AuthorDto> create(AuthorDto authorDto) {
        Author author = conversionService.convert(authorDto, Author.class);
        authorRepository.save(author);
        return Optional.of(conversionService.convert(author, AuthorDto.class));
    }

    @Override
    public Optional<AuthorDto> findById(UUID id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(value -> conversionService.convert(value, AuthorDto.class));
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new InvalidParameterException();
        }
        Optional<Author> deletedAuthor = authorRepository.findById(id);
        deletedAuthor.ifPresent(authorRepository::delete);
    }

    @Override
    public Optional<AuthorDto> updateByPut(AuthorDto authorDto, UUID id) {
        Author author = conversionService.convert(authorDto, Author.class);
        author.setId(id);
        authorRepository.save(author);
        return Optional.of(conversionService.convert(author, AuthorDto.class));
    }

    @Override
    public Set<AuthorDto> findByFirstLastMiddleName(String firstName, String lastName, String middleName) {
        return authorRepository.findByFirstLastMiddleName(firstName, lastName, middleName).stream()
                .map(author -> conversionService.convert(author, AuthorDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Page<AuthorDto> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(author -> conversionService.convert(author, AuthorDto.class));
    }

    @Override
    public Optional<AuthorDto> assignAuthorToBook(UUID idAuthor, UUID idBook) {
        Optional<AuthorDto> existedAuthor = findById(idAuthor);
        Optional<BookDto> existedBook = bookService.findById(idBook);
        if (existedAuthor.isPresent() && existedBook.isPresent()) {
            Author updatedAuthor = conversionService.convert(existedAuthor.get(), Author.class);
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedAuthor.getBooks().add(updatedBook);
            return updateByPut(conversionService.convert(updatedAuthor, AuthorDto.class), idAuthor);
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthorDto> unassignAuthorToBook(UUID idAuthor, UUID idBook) {
        Optional<AuthorDto> existedAuthor = findById(idAuthor);
        Optional<BookDto> existedBook = bookService.findById(idBook);
        if (existedAuthor.isPresent() && existedBook.isPresent()) {
            Author updatedAuthor = conversionService.convert(existedAuthor.get(), Author.class);
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedAuthor.getBooks().remove(updatedBook);
            return updateByPut(conversionService.convert(updatedAuthor, AuthorDto.class), idAuthor);
        }
        return Optional.empty();
    }

   @Override
    public Optional<BookDto> assignBookToAuthor(UUID idBook, UUID idAuthor) {
       Optional<BookDto> existedBook = bookService.findById(idBook);
       Optional<AuthorDto> existedAuthor = findById(idAuthor);
       if (existedBook.isPresent() && existedAuthor.isPresent()) {
           Author updatedAuthor = conversionService.convert(existedAuthor.get(), Author.class);
           Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
           updatedBook.getAuthors().add(updatedAuthor);
           return bookService.updateByPut(conversionService.convert(updatedBook, BookDto.class), idBook);
       }
       return Optional.empty();
    }

    @Override
    public Optional<BookDto> unassignBookToAuthor(UUID idBook, UUID idAuthor) {
        Optional<BookDto> existedBook = bookService.findById(idBook);
        Optional<AuthorDto> existedAuthor = findById(idAuthor);
        if (existedAuthor.isPresent() && existedBook.isPresent()) {
            Author updatedAuthor = conversionService.convert(existedAuthor.get(), Author.class);
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedBook.getAuthors().remove(updatedAuthor);
            return bookService.updateByPut(conversionService.convert(updatedBook, BookDto.class), idBook);
        }
        return Optional.empty();
    }
}
