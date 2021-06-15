package com.example.library.converter;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.entity.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorAuthorDtoConverter implements Converter<Author, AuthorDto> {
    @Override
    public AuthorDto convert(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .firstName(author.getNameEmbeddable().getFirstName())
                .middleName(author.getNameEmbeddable().getMiddleName())
                .lastName(author.getNameEmbeddable().getLastName())
                .build();
    }
}
