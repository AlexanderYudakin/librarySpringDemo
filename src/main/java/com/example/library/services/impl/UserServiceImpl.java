package com.example.library.services.impl;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.dto.UserDto;
import com.example.library.domain.entity.Book;
import com.example.library.domain.entity.User;
import com.example.library.domain.enums.UserType;
import com.example.library.repository.UserRepository;
import com.example.library.services.BookService;
import com.example.library.services.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final BookService bookService;

    @Override
    public Optional<UserDto> create(UserDto userDto) {
        if (findByEmail(userDto.getEmail()).isPresent()) {
            return Optional.empty();
        }
        User user = conversionService.convert(userDto, User.class);
        user.setUserType(UserType.ROLE_READER);
        userRepository.save(user);
        return Optional.of(conversionService.convert(user, UserDto.class));
    }

    @Override
    public Optional<UserDto> findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> conversionService.convert(value, UserDto.class));
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new InvalidParameterException();
        }
        Optional<User> deletedUser = userRepository.findById(id);
        deletedUser.ifPresent(userRepository::delete);
    }

    @Override
    public Optional<UserDto> updateByPut(UserDto userDto, UUID id) {
        User user = conversionService.convert(userDto, User.class);
        user.setId(id);
        user.setUserType(UserType.ROLE_READER);
        userRepository.save(user);
        return Optional.of(conversionService.convert(user, UserDto.class));
    }

    @Override
    public Set<UserDto> findByFirstLastMiddleName(String firstName, String lastName, String middleName) {
        return userRepository.findByFirstLastMiddleName(firstName, lastName, middleName).stream()
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return Optional.of(conversionService.convert(user.get(), UserDto.class));
        }
        return Optional.empty();
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> conversionService.convert(user, UserDto.class));
    }

    @Override
    public Optional<UserDto> assignUserToBook(UUID idUser, UUID idBook) {
        Optional<UserDto> existedUser = findById(idUser);
        Optional<BookDto> existedBook = bookService.findById(idBook);
        if (existedUser.isPresent() && existedBook.isPresent()) {
            User updatedUser = conversionService.convert(existedUser.get(), User.class);
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedUser.getBooks().add(updatedBook);
            return updateByPut(conversionService.convert(updatedUser, UserDto.class), idUser);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> unassignUserToBook(UUID idUser, UUID idBook) {
        Optional<UserDto> existedUser = findById(idUser);
        Optional<BookDto> existedBook = bookService.findById(idBook);
        if (existedUser.isPresent() && existedBook.isPresent()) {
            User updatedUser = conversionService.convert(existedUser.get(), User.class);
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedUser.getBooks().remove(updatedBook);
            return updateByPut(conversionService.convert(updatedUser, UserDto.class), idUser);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookDto> assignBookToUser(UUID idBook, UUID idUser) {
        Optional<BookDto> existedBook = bookService.findById(idBook);
        Optional<UserDto> existedUser = findById(idUser);
        if (existedBook.isPresent() && existedUser.isPresent()) {
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            User updatedUser = conversionService.convert(existedUser.get(), User.class);
            updatedBook.setReader(updatedUser);
            return bookService.updateByPut(conversionService.convert(updatedBook, BookDto.class), idBook);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookDto> unassignBookToUser(UUID idBook, UUID idUser) {
        Optional<BookDto> existedBook = bookService.findById(idBook);
        Optional<UserDto> existedUser = findById(idUser);
        if (existedBook.isPresent() && existedUser.isPresent()) {
            Book updatedBook = conversionService.convert(existedBook.get(), Book.class);
            updatedBook.setReader(null);
            return bookService.updateByPut(conversionService.convert(updatedBook, BookDto.class), idBook);
        }
        return Optional.empty();
    }
}
