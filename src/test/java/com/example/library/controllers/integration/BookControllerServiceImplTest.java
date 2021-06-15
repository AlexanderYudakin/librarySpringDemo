package com.example.library.controllers.integration;

import com.example.library.controller.BookController;
import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.dto.BookDto;
import com.example.library.domain.dto.UserDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.Book;
import com.example.library.domain.entity.NameEmbeddable;
import com.example.library.domain.entity.User;
import com.example.library.domain.enums.UserType;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.services.impl.AuthorServiceImpl;
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
public class BookControllerServiceImplTest {

    AuthorServiceImpl authorService;
    BookServiceImpl bookService;
    UserServiceImpl userService;

    BookController bookController;

    AuthorDto authorDto;
    BookDto bookDto;
    UserDto userDto;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService conversionService;

    Author author;
    private final UUID idAuthor = UUID.randomUUID();
    private final String firstNameAuthor  = "Lev";
    private final String lastNameAuthor   = "Tolstoy";
    private final String middleNameAuthor = "Nikolayevich";
    Book book;
    private final UUID idBook = UUID.randomUUID();
    private final String title = "Война и мир";
    private  ResponseEntity<BookDto> entryReturned;
    User user;
    private final UUID idUser = UUID.randomUUID();
    private final String firstNameUser  = "Ivan";
    private final String lastNameUser   = "Ivanov";
    private final String middleNameUser = "Ivanovich";
    private final String email = "wqet@greaf";

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(idAuthor);
        NameEmbeddable nameEmbeddableAuthor = new NameEmbeddable();
        nameEmbeddableAuthor.setFirstName(firstNameAuthor);
        nameEmbeddableAuthor.setMiddleName(lastNameAuthor);
        nameEmbeddableAuthor.setLastName(middleNameAuthor);
        author.setNameEmbeddable(nameEmbeddableAuthor);

        book = new Book();
        book.setId(idBook);
        book.setTitle(title);

        user = new User();
        user.setId(idUser);
        user.setEmail(email);
        NameEmbeddable nameEmbeddableUser = new NameEmbeddable();
        nameEmbeddableUser.setFirstName(firstNameAuthor);
        nameEmbeddableUser.setMiddleName(lastNameAuthor);
        nameEmbeddableUser.setLastName(middleNameAuthor);
        user.setNameEmbeddable(nameEmbeddableUser);

        authorDto = new AuthorDto(idAuthor, firstNameAuthor, lastNameAuthor, middleNameAuthor);
        bookDto = new BookDto(idBook, title);
        userDto = new UserDto(idUser, firstNameUser, lastNameUser, middleNameUser, UserType.ROLE_READER, email);

        bookService = new BookServiceImpl(bookRepository, conversionService);
        authorService = new AuthorServiceImpl(authorRepository, conversionService, bookService);
        userService = new UserServiceImpl(userRepository, conversionService, bookService);
        bookController = new BookController(authorService, bookService, userService);
    }

    @AfterEach
    void tearDown() {
        book.getAuthors().clear();
        book.setReader(null);
    }

    @Test
    public void assignBookToAuthorReturnedStatusOk() {
        Mockito.when(conversionService.convert(authorDto, Author.class)).thenReturn(author);
        Mockito.when(conversionService.convert(author, AuthorDto.class)).thenReturn(authorDto);
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);

        Mockito.when(authorRepository.findById(idAuthor)).thenReturn(Optional.of(author));
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        entryReturned = bookController.assignBookToAuthor(idBook, idAuthor);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, book.getAuthors().size()),
                () -> Assertions.assertTrue(book.getAuthors().contains(author)),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));
    }

    @Test
    public void assignBookToAuthorReturnedReturnedNotFound() {
        Mockito.when(authorRepository.findById(idAuthor)).thenReturn(Optional.empty());
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        entryReturned = bookController.assignBookToAuthor(idBook, idAuthor);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, book.getAuthors().size()),
                () -> Assertions.assertNull(bookDtoReturned));
    }

    @Test
    public void unassignBookToAuthorReturnedStatusOk() {
        Mockito.when(conversionService.convert(authorDto, Author.class)).thenReturn(author);
        Mockito.when(conversionService.convert(author, AuthorDto.class)).thenReturn(authorDto);
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);

        Mockito.when(authorRepository.findById(idAuthor)).thenReturn(Optional.of(author));
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        book.getAuthors().add(author);

        entryReturned = bookController.unassignBookToAuthor(idBook, idAuthor);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(0, book.getAuthors().size()),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));
    }

    @Test
    public void unassignBookToAuthorReturnedNotFound() {
        Mockito.when(authorRepository.findById(idAuthor)).thenReturn(Optional.empty());
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        book.getAuthors().add(author);

        entryReturned = bookController.unassignBookToAuthor(idBook, idAuthor);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to author was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(1, book.getAuthors().size()),
                () -> Assertions.assertTrue(book.getAuthors().contains(author)),
                () -> Assertions.assertNull(bookDtoReturned));
    }

    @Test
    public void assignBookToUserReturnedStatusOk() {
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);
        Mockito.when(conversionService.convert(userDto, User.class)).thenReturn(user);
        Mockito.when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        entryReturned = bookController.assignBookToUser(idBook, idUser);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(user, book.getReader()),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));
    }

    @Test
    public void assignBookToUserReturnedNotFound() {
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        entryReturned = bookController.assignBookToUser(idBook, idUser);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Assign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(null, book.getReader()),
                () -> Assertions.assertNull(bookDtoReturned));
    }

    @Test
    public void unassignBookToUserReturnedStatusOk() {
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);
        Mockito.when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        book.setReader(user);

        entryReturned = bookController.unassignBookToUser(idBook, idUser);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(null, book.getReader()),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));
    }

    @Test
    public void unassignBookToUserReturnedNotFound() {
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        book.setReader(user);

        entryReturned = bookController.unassignBookToUser(idBook, idUser);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Unassign book to user was failed",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(user, book.getReader()),
                () -> Assertions.assertNull(bookDtoReturned));
    }

    @Test
    public void createBookServiceAvailable() {
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);

        ResponseEntity<BookDto> entryReturned = bookController.create(bookDto);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Create book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    public void getFindByIdResponseFound() {
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        ResponseEntity<?> entryReturned = bookController.findById(idBook);
        Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode());

        Object bookDtoReturned = entryReturned.getBody();
        Assertions.assertEquals(bookDto, bookDtoReturned);
    }

    @Test
    public void getFindByIdNotFound() {
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        ResponseEntity<?> entryReturned = bookController.findById(idBook);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, entryReturned.getStatusCode());

        Object bookDtoReturned = entryReturned.getBody();
        Assertions.assertEquals("Book is not found, id = " + idBook, bookDtoReturned.toString());
    }

    @Test
    public void updatePutResponseFound() {
        Mockito.when(conversionService.convert(bookDto, Book.class)).thenReturn(book);
        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);

        ResponseEntity<BookDto> entryReturned = bookController.updatePut(bookDto, idBook);
        BookDto bookDtoReturned = entryReturned.getBody();

        Assertions.assertAll(
                "Update book was failed",
                () -> Assertions.assertEquals(HttpStatus.OK, entryReturned.getStatusCode()),
                () -> Assertions.assertEquals(bookDto, bookDtoReturned));

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    public void delete() {
        Mockito.when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));

        bookController.delete(idBook);
        Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    public void getByTitle() {
        Set<Book> books = new HashSet<>();
        books.add(book);

        Set<BookDto> booksDto = new HashSet<>();
        booksDto.add(bookDto);

        Mockito.when(conversionService.convert(book, BookDto.class)).thenReturn(bookDto);

        Mockito.when(bookRepository.findByTitle(title)).thenReturn(books);

        Set<BookDto> findBooksDto = bookController.getByTitle(title);
        Assertions.assertEquals(booksDto, findBooksDto);
    }

    @Test
    public void getByTitleNotFound() {
        Set<Book> books = new HashSet<>();
        Set<BookDto> booksDto = new HashSet<>();

        Mockito.when(bookRepository.findByTitle(title)).thenReturn(books);

        Set<BookDto> findBooksDto = bookController.getByTitle(title);
        Assertions.assertEquals(booksDto, findBooksDto);
    }

    @Test
    public void getAll() {
        Page<Book> page = new PageImpl<>(Collections.singletonList(book));

        doReturn(bookDto).when(conversionService).convert(book, BookDto.class);
        doReturn(page).when(bookRepository).findAll(Pageable.unpaged());

        Page<BookDto> all = bookController.getAll(Pageable.unpaged());
        Assertions.assertEquals(1, all.getSize());
        Assertions.assertEquals(bookDto, all.getContent().get(0));
    }
}
