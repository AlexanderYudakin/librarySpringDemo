package com.example.library.controller;

import com.example.library.domain.dto.UserDto;
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
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        Optional<UserDto> user = userService.create(userDto);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID id) {
        Optional<UserDto> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User is not found, id = " + id.toString(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public Page<UserDto> getAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updatePut(
            @RequestBody UserDto userDto,
            @PathVariable("id") UUID id
    ) {
        Optional<UserDto> user = userService.updateByPut(userDto, id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        userService.delete(id);
    }

    @GetMapping("/byFirstName/{firstName}/byLastName/{lastName}/byMiddleName/{middleName}")
    public Set<UserDto> getByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @PathVariable("middleName") String middleName) {
        return userService.findByFirstLastMiddleName(firstName, lastName, middleName);
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable("email") String email) {
        Optional<UserDto> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idUser}/assignBook/{idBook}")
    @PreAuthorize(value = "hasRole('READER') || hasAuthority('READER')")
    public ResponseEntity<UserDto> assignUserToBook(
            @PathVariable UUID idUser, @PathVariable UUID idBook) {
        Optional<UserDto> user = userService.assignUserToBook(idUser, idBook);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idUser}/unassignBook/{idBook}")
    @PreAuthorize(value = "hasRole('READER') || hasAuthority('READER')")
    public ResponseEntity<UserDto> unassignUserToBook(
            @PathVariable UUID idUser, @PathVariable UUID idBook) {
        Optional<UserDto> user = userService.unassignUserToBook(idUser, idBook);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
