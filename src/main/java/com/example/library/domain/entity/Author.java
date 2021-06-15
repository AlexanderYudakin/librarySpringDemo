package com.example.library.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author extends EntityBase {

    @Embedded
    private NameEmbeddable nameEmbeddable;

    @ManyToMany
    @JoinColumn(name = "book_uuid")
    private Set<Book> books = new HashSet<>();
}
