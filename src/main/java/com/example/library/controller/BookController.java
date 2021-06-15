package com.example.library.controller;

import com.example.library.domain.dto.BookDto;
import com.example.library.services.AuthorService;
import com.example.library.services.BookService;
import com.example.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        Optional<BookDto> book = bookService.create(bookDto);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id) {
        Optional<BookDto> book = bookService.findById(id);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not found, id = " + id.toString(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updatePut(
            @RequestBody BookDto bookDto,
            @PathVariable("id") UUID id
    ) {
        Optional<BookDto> book = bookService.updateByPut(bookDto, id);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        bookService.delete(id);
    }

    @GetMapping("/byTitle/{title}")
    public Set<BookDto> getByTitle(@PathVariable("title") String title) {
        return bookService.getByTitle(title);
    }

    @PutMapping("/{idBook}/assignAuthor/{idAuthor}")
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookDto> assignBookToAuthor(
            @PathVariable UUID idBook, @PathVariable UUID idAuthor) {
        Optional<BookDto> book = authorService.assignBookToAuthor(idBook, idAuthor);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idBook}/unassignAutor/{idAuthor}")
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookDto> unassignBookToAuthor(
            @PathVariable UUID idBook, @PathVariable UUID idAuthor) {
        Optional<BookDto> book = authorService.unassignBookToAuthor(idBook, idAuthor);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idBook}/assignUser/{idUser}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<BookDto> assignBookToUser(
            @PathVariable UUID idBook, @PathVariable UUID idUser) {
        Optional<BookDto> book = userService.assignBookToUser(idBook, idUser);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idBook}/unassignUser/{idUser}")
    @PreAuthorize(value = "hasRole('READER') || hasAuthority('ROLE_READER')")
    public ResponseEntity<BookDto> unassignBookToUser(
            @PathVariable UUID idBook, @PathVariable UUID idUser) {
        Optional<BookDto> book = userService.unassignBookToUser(idBook, idUser);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
