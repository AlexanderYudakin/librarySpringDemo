package com.example.library.controllers.integration;

import com.example.library.DemoApplication;
import com.example.library.controller.BookController;
import com.example.library.controller.UserController;
import com.example.library.domain.dto.BookDto;
import com.example.library.domain.dto.UserDto;
import com.example.library.domain.enums.UserType;
import com.example.library.services.impl.BookServiceImpl;
import com.example.library.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@SpringBootTest(classes = DemoApplication.class)
public class RestIntegrationTestUserAndBook {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    UserController userController;

    @Autowired
    BookController bookController;

    UserDto userDto;
    BookDto bookDto;

    private final String firstName  = "Ivan";
    private final String lastName   = "Ivanov";
    private final String middleName = "Ivanovich";
    private final String firstNameNew  = "Vasily";
    private final String lastNameNew   = "Vasiliev";
    private final String middleNameNew = "Vasilievich";
    private final String email= "wqet@greaf";
    private final String emailNew= "asfg@wett";
    private final UserType userType = UserType.ROLE_READER;
    private final String title = "Война и мир";
    private final String titleNew = "Отцы и дети";

    @BeforeEach
    void setUp() {
        userDto = new UserDto(null, firstName, lastName, middleName, userType, email);
        bookDto = new BookDto(null, title);
    }

    @Test
    public void testAssociationUserAndBook() {
        createUser();
        createSecondUser();
        createBook();
        assignUserToBook();
        assignBookToUser();
        updateUserAndBookDto();
        updatePutUser();
        updatePutBook();
        getByNameUser();
        getByEmailUser();
        getByTitle();
        getAllUsers();
        unassignBookToUser();
        unassignUserToBook();
        deleteBook();
        deleteUser();
        getFindByIdUser();
        getFindByIdBook();
    }

    public void createUser() {
        ResponseEntity<UserDto> entryReturned = userController.create(userDto);
        UserDto userDtoReturned = entryReturned.getBody();

        userDto.setId(userDtoReturned.getId());

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto.getFirstName(), userDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(userDto.getLastName(), userDtoReturned.getLastName()),
                () -> Assertions.assertEquals(userDto.getMiddleName(), userDtoReturned.getMiddleName()),
                () -> Assertions.assertEquals(userDto.getEmail(), userDtoReturned.getEmail()));
    }

    public void createSecondUser() {
        ResponseEntity<UserDto> entryReturned = userController.create(userDto);
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.CONFLICT, entryReturned.getStatusCode()),
                () -> Assertions.assertNull(userDtoReturned));
    }

    public void createBook() {
        ResponseEntity<BookDto> entryReturned = bookController.create(bookDto);
        BookDto bookDtoReturned = entryReturned.getBody();

        bookDto.setId(bookDtoReturned.getId());

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void assignUserToBook() {
        ResponseEntity<UserDto> entryReturned = userController.assignUserToBook(userDto.getId(), bookDto.getId());
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Create user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto.getFirstName(), userDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(userDto.getLastName(), userDtoReturned.getLastName()),
                () -> Assertions.assertEquals(userDto.getMiddleName(), userDtoReturned.getMiddleName()),
                () -> Assertions.assertEquals(userDto.getEmail(), userDtoReturned.getEmail()));
    }

    public void assignBookToUser() {
        ResponseEntity<BookDto> entryReturned = bookController.assignBookToUser(bookDto.getId(), userDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void updateUserAndBookDto() {
        userDto.setFirstName(firstNameNew);
        userDto.setLastName(lastNameNew);
        userDto.setMiddleName(middleNameNew);
        userDto.setEmail(emailNew);
        bookDto.setTitle(titleNew);
    }

    public void updatePutUser() {
        ResponseEntity<UserDto> entryReturned = userController.updatePut(userDto, userDto.getId());
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto.getFirstName(), userDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(userDto.getLastName(), userDtoReturned.getLastName()),
                () -> Assertions.assertEquals(userDto.getMiddleName(), userDtoReturned.getMiddleName()),
                () -> Assertions.assertEquals(userDto.getEmail(), userDtoReturned.getEmail()));
    }

    public void updatePutBook() {
        ResponseEntity<BookDto> entryReturned = bookController.updatePut(bookDto, bookDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void getByNameUser() {
        Set<UserDto> users = userController.getByName(userDto.getFirstName(), userDto.getLastName(), userDto.getMiddleName());

        Assertions.assertAll(
                "Get by name user was failed",
                () -> Assertions.assertEquals(1, users.size()),
                () -> Assertions.assertTrue(users.contains(userDto)));
    }

    public void getByEmailUser() {
        ResponseEntity<UserDto> entryReturned = userController.getByEmail(userDto.getEmail());
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Get by email user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto.getFirstName(), userDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(userDto.getLastName(), userDtoReturned.getLastName()),
                () -> Assertions.assertEquals(userDto.getMiddleName(), userDtoReturned.getMiddleName()),
                () -> Assertions.assertEquals(userDto.getEmail(), userDtoReturned.getEmail()));
    }

    public void getByTitle() {
        Set<BookDto> books = bookController.getByTitle(bookDto.getTitle());

        Assertions.assertAll(
                "Get by title book was failed",
                () -> Assertions.assertEquals(1, books.size()),
                () -> Assertions.assertTrue(books.contains(bookDto)));
    }

    public void getAllUsers() {
        Page<UserDto> all = userController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(userDto, all.getContent().get(0));
    }

    public void unassignBookToUser() {
        ResponseEntity<BookDto> entryReturned = bookController.unassignBookToUser(bookDto.getId(), userDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void unassignUserToBook() {
        ResponseEntity<UserDto> entryReturned = userController.unassignUserToBook(userDto.getId(), bookDto.getId());
        UserDto userDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign user to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(userDto.getFirstName(), userDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(userDto.getLastName(), userDtoReturned.getLastName()),
                () -> Assertions.assertEquals(userDto.getMiddleName(), userDtoReturned.getMiddleName()),
                () -> Assertions.assertEquals(userDto.getEmail(), userDtoReturned.getEmail()));
    }

    public void deleteBook() {
        bookController.delete(bookDto.getId());
    }

    public void deleteUser() {
        userController.delete(userDto.getId());
    }

    public void getFindByIdUser() {
        ResponseEntity<?> entryReturned = userController.findById(userDto.getId());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object userDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("User is not found, id = " + userDto.getId(), userDtoReturned.toString());
    }

    public void getFindByIdBook() {
        ResponseEntity<?> entryReturned = bookController.findById(bookDto.getId());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object bookDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("Book is not found, id = " + bookDto.getId(), bookDtoReturned.toString());
    }
}
