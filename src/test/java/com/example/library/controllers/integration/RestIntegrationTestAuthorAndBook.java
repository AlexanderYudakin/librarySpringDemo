package com.example.library.controllers.integration;

import com.example.library.DemoApplication;
import com.example.library.controller.AuthorController;
import com.example.library.controller.BookController;
import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.dto.BookDto;
import com.example.library.services.impl.AuthorServiceImpl;
import com.example.library.services.impl.BookServiceImpl;
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
public class RestIntegrationTestAuthorAndBook {

    @Autowired
    AuthorServiceImpl authorService;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    AuthorController authorController;

    @Autowired
    BookController bookController;


    AuthorDto authorDto;
    BookDto bookDto;

    private final String firstName  = "Lev";
    private final String lastName   = "Tolstoy";
    private final String middleName = "Nikolayevich";
    private final String firstNameNew  = "Ivan";
    private final String lastNameNew   = "Turgenev";
    private final String middleNameNew = "Sergeyevich";
    private final String title = "Война и мир";
    private final String titleNew = "Отцы и дети";

    @BeforeEach
    void setUp() {
        authorDto = new AuthorDto(null, firstName, lastName, middleName);
        bookDto = new BookDto(null, title);
    }

    @Test
    public void testAssociationAuthorAndBook() {
        createAuthor();
        createBook();
        assignAuthorToBook();
        assignBookToAuthor();
        updateAuthorAndBookDto();
        updatePutAuthor();
        updatePutBook();
        getByNameAuthor();
        getByTitle();
        getAllAuthors();
        getAllBooks();
        unassignBookToAuthor();
        unassignAuthorToBook();
        deleteBook();
        deleteAuthor();
        getFindByIdAuthor();
        getFindByIdBook();
    }

    public void createAuthor() {
        ResponseEntity<AuthorDto> entryReturned = authorController.create(authorDto);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        authorDto.setId(authorDtoReturned.getId());

        Assertions.assertAll(
                "Create author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto.getFirstName(), authorDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(authorDto.getLastName(), authorDtoReturned.getLastName()),
                () -> Assertions.assertEquals(authorDto.getMiddleName(), authorDtoReturned.getMiddleName()));
    }

    public void createBook() {
        ResponseEntity<BookDto> entryReturned = bookController.create(bookDto);
        BookDto bookDtoReturned = entryReturned.getBody();

        bookDto.setId(bookDtoReturned.getId());

        Assertions.assertAll(
                "Create author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void assignAuthorToBook() {
        ResponseEntity<AuthorDto> entryReturned = authorController.assignAuthorToBook(authorDto.getId(), bookDto.getId());
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto.getFirstName(), authorDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(authorDto.getLastName(), authorDtoReturned.getLastName()),
                () -> Assertions.assertEquals(authorDto.getMiddleName(), authorDtoReturned.getMiddleName()));
    }

    public void assignBookToAuthor() {
        ResponseEntity<BookDto> entryReturned = bookController.assignBookToAuthor(bookDto.getId(), authorDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void updateAuthorAndBookDto() {
        authorDto.setFirstName(firstNameNew);
        authorDto.setLastName(lastNameNew);
        authorDto.setMiddleName(middleNameNew);
        bookDto.setTitle(titleNew);
    }

    public void updatePutAuthor() {
        ResponseEntity<AuthorDto> entryReturned = authorController.updatePut(authorDto, authorDto.getId());
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto.getFirstName(), authorDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(authorDto.getLastName(), authorDtoReturned.getLastName()),
                () -> Assertions.assertEquals(authorDto.getMiddleName(), authorDtoReturned.getMiddleName()));

    }

    public void updatePutBook() {
        ResponseEntity<BookDto> entryReturned = bookController.updatePut(bookDto, bookDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void getByNameAuthor() {
        Set<AuthorDto> authors = authorController.getByName(authorDto.getFirstName(), authorDto.getLastName(), authorDto.getMiddleName());

        Assertions.assertAll(
                "Get by name author was failed",
                () -> Assertions.assertEquals(1, authors.size()),
                () -> Assertions.assertTrue(authors.contains(authorDto)));
    }

    public void getByTitle() {
        Set<BookDto> books = bookController.getByTitle(bookDto.getTitle());

        Assertions.assertAll(
                "Get by title book was failed",
                () -> Assertions.assertEquals(1, books.size()),
                () -> Assertions.assertTrue(books.contains(bookDto)));
    }

    public void getAllAuthors() {
        Page<AuthorDto> all = authorController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(authorDto, all.getContent().get(0));
    }

    public void getAllBooks() {
        Page<BookDto> all = bookController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(bookDto, all.getContent().get(0));
    }

    public void unassignBookToAuthor() {
        ResponseEntity<BookDto> entryReturned = bookController.unassignBookToAuthor(bookDto.getId(), authorDto.getId());
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto.getTitle(), bookDtoReturned.getTitle()));
    }

    public void unassignAuthorToBook() {
        ResponseEntity<AuthorDto> entryReturned = authorController.unassignAuthorToBook(authorDto.getId(), bookDto.getId());
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto.getFirstName(), authorDtoReturned.getFirstName()),
                () -> Assertions.assertEquals(authorDto.getLastName(), authorDtoReturned.getLastName()),
                () -> Assertions.assertEquals(authorDto.getMiddleName(), authorDtoReturned.getMiddleName()));
    }

    public void deleteBook() {
        bookController.delete(bookDto.getId());
    }

    public void deleteAuthor() {
        authorController.delete(authorDto.getId());
    }

    public void getFindByIdAuthor() {
        ResponseEntity<?> entryReturned = authorController.findById(authorDto.getId());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object authorDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("Author is not found, id = " + authorDto.getId(), authorDtoReturned.toString());
    }

    public void getFindByIdBook() {
        ResponseEntity<?> entryReturned = bookController.findById(bookDto.getId());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object bookDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("Book is not found, id = " + bookDto.getId(), bookDtoReturned.toString());
    }
}
