package com.example.library.controllers.integration;

import com.example.library.controller.AuthorController;
import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.Book;
import com.example.library.domain.entity.NameEmbeddable;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.services.impl.AuthorServiceImpl;
import com.example.library.services.impl.BookServiceImpl;
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
public class AuthorControllerServiceImplTest {

    AuthorServiceImpl authorService;
    BookServiceImpl bookService;

    AuthorController authorController;

    AuthorDto authorDto;
    BookDto bookDto;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ConversionService conversionService;

    Author author;
    private final UUID idAuthor = UUID.randomUUID();
    private final String firstName  = "Lev";
    private final String lastName   = "Tolstoy";
    private final String middleName = "Nikolayevich";
    Book book;
    private final UUID idBook = UUID.randomUUID();
    private final String title = "Война и мир";
    private  ResponseEntity<AuthorDto> entryReturned;


    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(idAuthor);
        NameEmbeddable nameEmbeddable = new NameEmbeddable();
        nameEmbeddable.setFirstName(firstName);
        nameEmbeddable.setMiddleName(lastName);
        nameEmbeddable.setLastName(middleName);
        author.setNameEmbeddable(nameEmbeddable);

        book = new Book();
        book.setId(idBook);
        book.setTitle(title);

        authorDto = new AuthorDto(idAuthor, firstName, lastName, middleName);
        bookDto = new BookDto(idBook, title);

        bookService = new BookServiceImpl(bookRepository, conversionService);
        authorService = new AuthorServiceImpl(authorRepository, conversionService, bookService);
        authorController = new AuthorController(authorService);
    }

    @AfterEach
    void tearDown() {
        author.getBooks().clear();
    }

    @Test
    public void assignAuthorToBookReturnedStatusOk() {
        doReturn(author).when(conversionService).convert(authorDto, Author.class);
        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);
        doReturn(book).when(conversionService).convert(bookDto, Book.class);
        doReturn(bookDto).when(conversionService).convert(book, BookDto.class);

        doReturn(Optional.of(author)).when(authorRepository).findById(idAuthor);
        doReturn(Optional.of(book)).when(bookRepository).findById(idBook);

        entryReturned = authorController.assignAuthorToBook(idAuthor, idBook);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, author.getBooks().size()),
                () -> Assertions.assertTrue(author.getBooks().contains(book)),
                () -> Assertions.assertEquals(authorDto, authorDtoReturned));
    }

    @Test
    public void assignAuthorToBookReturnedNotFound() {
        doReturn(Optional.empty()).when(authorRepository).findById(idAuthor);
        doReturn(Optional.empty()).when(bookRepository).findById(idBook);

        entryReturned = authorController.assignAuthorToBook(idAuthor, idBook);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, author.getBooks().size()),
                () -> Assertions.assertNull(authorDtoReturned));
    }

    @Test
    public void unassignAuthorToBookReturnedStatusOk() {
        doReturn(author).when(conversionService).convert(authorDto, Author.class);
        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);
        doReturn(book).when(conversionService).convert(bookDto, Book.class);
        doReturn(bookDto).when(conversionService).convert(book, BookDto.class);

        doReturn(Optional.of(author)).when(authorRepository).findById(idAuthor);
        doReturn(Optional.of(book)).when(bookRepository).findById(idBook);

        author.getBooks().add(book);

        entryReturned = authorController.unassignAuthorToBook(idAuthor, idBook);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, author.getBooks().size()),
                () -> Assertions.assertEquals(authorDto, authorDtoReturned));
    }

    @Test
    public void unassignAuthorToBookReturnedNotFound() {
        doReturn(Optional.empty()).when(authorRepository).findById(idAuthor);
        doReturn(Optional.empty()).when(bookRepository).findById(idBook);

        author.getBooks().add(book);

        entryReturned = authorController.unassignAuthorToBook(idAuthor, idBook);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign author to book was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, author.getBooks().size()),
                () -> Assertions.assertTrue(author.getBooks().contains(book)),
                () -> Assertions.assertNull(authorDtoReturned));
    }

    @Test
    public void getFindByIdResponseFound() {
        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);
        doReturn(Optional.of(author)).when(authorRepository).findById(idAuthor);

        ResponseEntity<?> entryReturned = authorController.findById(idAuthor);
        Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode());

        Object authorDtoReturned = entryReturned.getBody();
        Assertions.assertEquals(authorDto, authorDtoReturned);
    }

    @Test
    public void getFindByIdNotFound() {
        doReturn(Optional.empty()).when(authorRepository).findById(idAuthor);

        ResponseEntity<?> entryReturned = authorController.findById(idAuthor);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object authorDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("Author is not found, id = " + idAuthor, authorDtoReturned.toString());
    }

    @Test
    public void createAuthorServiceAvailable() {
        doReturn(author).when(conversionService).convert(authorDto, Author.class);
        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);

        ResponseEntity<AuthorDto> entryReturned = authorController.create(authorDto);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Create author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto, authorDtoReturned));

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
    }

    @Test
    public void updatePutResponseFound() {
        doReturn(author).when(conversionService).convert(authorDto, Author.class);
        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);

        ResponseEntity<AuthorDto> entryReturned = authorController.updatePut(authorDto, idAuthor);
        AuthorDto authorDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(authorDto, authorDtoReturned));

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
    }

   @Test
    public void delete() {
        doReturn(Optional.of(author)).when(authorRepository).findById(idAuthor);

        authorController.delete(idAuthor);
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
    }

    @Test
    public void getByName() {
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Set<AuthorDto> authorsDto = new HashSet<>();
        authorsDto.add(authorDto);

        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);

        doReturn(authors).when(authorRepository).findByFirstLastMiddleName(firstName, lastName, middleName);

        Set<AuthorDto> findUsersDto = authorController.getByName(firstName, lastName, middleName);
        Assertions.assertEquals(authorsDto, findUsersDto);
    }

    @Test
    public void getByNameNotFound() {
        Set<Author> authors = new HashSet<>();

        Set<AuthorDto> authorsDto = new HashSet<>();

        doReturn(authors).when(authorRepository).findByFirstLastMiddleName(firstName, lastName, middleName);

        Set<AuthorDto> findAuthorsDto = authorController.getByName(firstName, lastName, middleName);
        Assertions.assertEquals(authorsDto, findAuthorsDto);
    }

    @Test
    public void getAll() {

        Page<Author> page = new PageImpl<>(Collections.singletonList(author));

        doReturn(authorDto).when(conversionService).convert(author, AuthorDto.class);
        doReturn(page).when(authorRepository).findAll(Pageable.unpaged());

        Page<AuthorDto> all = authorController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(authorDto, all.getContent().get(0));
    }
}
