package com.example.library.converter;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookDtoBookConverter implements Converter<BookDto, Book>  {
    @Override
    public Book convert(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        return book;
    }
}
