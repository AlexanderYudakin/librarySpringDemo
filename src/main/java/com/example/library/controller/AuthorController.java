package com.example.library.controller;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        Optional<AuthorDto> author = authorService.create(authorDto);
        if (author.isPresent()) {
            return new ResponseEntity<>(author.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id) {
        Optional<AuthorDto> author = authorService.findById(id);
        if (author.isPresent()) {
            return new ResponseEntity<>(author.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Author is not found, id = " +id.toString(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public Page<AuthorDto> getAll(Pageable pageable) {
        return authorService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updatePut(
            @RequestBody AuthorDto authorDto,
            @PathVariable("id") UUID id
    ) {
        Optional<AuthorDto> author = authorService.updateByPut(authorDto, id);
        if (author.isPresent()) {
            return new ResponseEntity<>(author.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        authorService.delete(id);
    }

    @GetMapping("/byFirstName/{firstName}/byLastName/{lastName}/byMiddleName/{middleName}")
    public Set<AuthorDto> getByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @PathVariable("middleName") String middleName) {
        return authorService.findByFirstLastMiddleName(firstName, lastName, middleName);
    }

    @PutMapping("/{idAuthor}/assignBook/{idBook}")
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AuthorDto> assignAuthorToBook(
            @PathVariable UUID idAuthor, @PathVariable UUID idBook) {
        Optional<AuthorDto> author = authorService.assignAuthorToBook(idAuthor, idBook);
        if (author.isPresent()) {
            return new ResponseEntity<>(author.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idAuthor}/unassignBook/{idBook}")
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AuthorDto> unassignAuthorToBook(
            @PathVariable UUID idAuthor, @PathVariable UUID idBook) {
        Optional<AuthorDto> author = authorService.unassignAuthorToBook(idAuthor, idBook);
        if (author.isPresent()) {
            return new ResponseEntity<>(author.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
