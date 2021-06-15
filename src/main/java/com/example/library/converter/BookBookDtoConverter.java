package com.example.library.converter;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookBookDtoConverter implements Converter<Book, BookDto> {
    @Override
    public BookDto convert(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build();
    }
}
