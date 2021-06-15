package com.example.library.converter;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.NameEmbeddable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoAuthorConverter implements Converter<AuthorDto, Author> {
    @Override
    public Author convert(AuthorDto authorDto) {
        Author author = new Author();
        NameEmbeddable nameEmbeddable = new NameEmbeddable();
        nameEmbeddable.setFirstName(authorDto.getFirstName());
        nameEmbeddable.setMiddleName(authorDto.getMiddleName());
        nameEmbeddable.setLastName(authorDto.getLastName());
        author.setNameEmbeddable(nameEmbeddable);
        return author;
    }
}
