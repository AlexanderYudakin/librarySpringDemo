package com.example.library.controllers.integration;

import com.example.library.controller.UserController;
import com.example.library.domain.dto.BookDto;
import com.example.library.domain.dto.UserDto;
import com.example.library.domain.entity.Book;
import com.example.library.domain.entity.NameEmbeddable;
import com.example.library.domain.entity.User;
import com.example.library.domain.enums.UserType;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.services.impl.BookServiceImpl;
import com.example.library.services.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserControllerServiceImplTest {
    UserServiceImpl userService;
    BookServiceImpl bookService;

    UserController userController;

    UserDto userDto;
    BookDto bookDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ConversionService conversionService;

    Book book;
    private final UUID idBook = UUID.randomUUID();
    private final String title = "Война и мир";

    User user;
    private final UUID idUser = UUID.randomUUID();
    private final String firstName  = "Ivan";
    private final String lastName   = "Ivanov";
    private final String middleName = "Ivanovich";
    private final String email = "wqet@greaf";

    private ResponseEntity<UserDto> entryReturned;


    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(idBook);
        book.setTitle(title);

        user = new User();
        user.setId(idUser);
        user.setEmail(email);
        NameEmbeddable nameEmbeddableUser = new NameEmbeddable();
        nameEmbeddableUser.setFirstName(firstName);
        nameEmbeddableUser.setMiddleName(lastName);
        nameEmbeddableUser.setLastName(middleName);
        user.setNameEmbeddable(nameEmbeddableUser);

        bookDto = new BookDto(idBook, title);
        userDto = new UserDto(idUser, firstName, lastName, middleName, UserType.ROLE_READER, email);

        bookService = new BookServiceImpl(bookRepository, conversionService);
        userService = new UserServiceImpl(userRepository, conversionService, bookService);
        userController = new UserController(userService);
    }

    @AfterEach
    void tearDown() {
        user.getBooks().clear();
    }

    @Test
    public void assignUserToBookReturnedStatusOk() {
        doReturn(user).when(conversionService).convert(userDto, User.class);
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);
        doReturn(book).when(conversionService).convert(bookDto, Book.class);
        doReturn(bookDto).when(conversionService).convert(book, BookDto.class);

        doReturn(Optional.of(user)).when(userRepository).findById(idUser);
        doReturn(Optional.of(book)).when(bookRepository).findById(idBook);

        entryReturned = userController.assignUserToBook(idUser, idBook);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign user to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, user.getBooks().size()),
                () -> Assertions.assertTrue(user.getBooks().contains(book)),
                () -> Assertions.assertEquals(userDto, userDtoReturned));
    }

    @Test
    public void assignUserToBookReturnedNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(idUser);
        doReturn(Optional.empty()).when(bookRepository).findById(idBook);

        entryReturned = userController.assignUserToBook(idUser, idBook);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, user.getBooks().size()),
                () -> Assertions.assertNull(userDtoReturned));
    }

    @Test
    public void unassignUserToBookReturnedStatusOk() {
        doReturn(user).when(conversionService).convert(userDto, User.class);
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);
        doReturn(book).when(conversionService).convert(bookDto, Book.class);
        doReturn(bookDto).when(conversionService).convert(book, BookDto.class);

        doReturn(Optional.of(user)).when(userRepository).findById(idUser);
        doReturn(Optional.of(book)).when(bookRepository).findById(idBook);

        user.getBooks().add(book);

        entryReturned = userController.unassignUserToBook(idUser, idBook);
        UserDto userDtoReturned = entryReturned.getBody();
        Assertions.assertAll(
                "Unassign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, user.getBooks().size()),
                () -> Assertions.assertEquals(userDto, userDtoReturned));
    }

    @Test
    public void unassignUserToBookReturnedNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(idUser);
        doReturn(Optional.empty()).when(bookRepository).findById(idBook);

        user.getBooks().add(book);

        entryReturned = userController.unassignUserToBook(idUser, idBook);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign user to book was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, user.getBooks().size()),
                () -> Assertions.assertTrue(user.getBooks().contains(book)),
                () -> Assertions.assertEquals(null, userDtoReturned));
    }

    @Test
    public void getFindByIdResponseFound() {
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);
        doReturn(Optional.of(user)).when(userRepository).findById(idUser);

        ResponseEntity<?> entryReturned = userController.findById(idUser);
        Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode());

        Object userDtoReturned = entryReturned.getBody();
        Assertions.assertEquals(userDto, userDtoReturned);
    }

    @Test
    public void getFindByIdNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(idUser);

        ResponseEntity<?> entryReturned = userController.findById(idUser);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object userDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("User is not found, id = " + idUser, userDtoReturned.toString());
    }

    @Test
    public void createUserServiceAvailable() {
        doReturn(user).when(conversionService).convert(userDto, User.class);
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);

        ResponseEntity<UserDto> entryReturned = userController.create(userDto);
        UserDto userDtoReturned = entryReturned.getBody();

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto, userDtoReturned));
    }

    @Test
    public void updatePutResponseFound() {
        doReturn(user).when(conversionService).convert(userDto, User.class);
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);

        ResponseEntity<UserDto> entryReturned = userController.updatePut(userDto, idUser);
        UserDto userDtoReturned = entryReturned.getBody();

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto, userDtoReturned));
    }

    @Test
    public void delete() {
        doReturn(Optional.of(user)).when(userRepository).findById(idUser);

        userController.delete(idUser);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    public void getByName() {
        Set<User> users = new HashSet<>();
        users.add(user);

        Set<UserDto> usersDto = new HashSet<>();
        usersDto.add(userDto);

        doReturn(userDto).when(conversionService).convert(user, UserDto.class);

        doReturn(users).when(userRepository).findByFirstLastMiddleName(firstName, lastName, middleName);

        Set<UserDto> findUsersDto = userController.getByName(firstName, lastName, middleName);
        Assertions.assertEquals(usersDto, findUsersDto);
    }

    @Test
    public void getByNameNotFound() {
        Set<User> users = new HashSet<>();

        Set<UserDto> usersDto = new HashSet<>();

        doReturn(users).when(userRepository).findByFirstLastMiddleName(firstName, lastName, middleName);

        Set<UserDto> findUsersDto = userController.getByName(firstName, lastName, middleName);
        Assertions.assertEquals(usersDto, findUsersDto);
    }

    @Test
    public void getByEmailReturnedStatusOk() {
        doReturn(userDto).when(conversionService).convert(user, UserDto.class);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(email);

        ResponseEntity<UserDto> entryReturned = userController.getByEmail(email);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Get by name was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto, userDtoReturned));
    }

    @Test
    public void getByEmailReturnedNotFound() {
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        ResponseEntity<UserDto> entryReturned = userController.getByEmail(email);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Get by name was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertNull(userDtoReturned));
    }

    @Test
    public void getAll() {
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

        doReturn(userDto).when(conversionService).convert(user, UserDto.class);
        doReturn(page).when(userRepository).findAll(Pageable.unpaged());

        Page<UserDto> all = userController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(userDto, all.getContent().get(0));
    }
}
