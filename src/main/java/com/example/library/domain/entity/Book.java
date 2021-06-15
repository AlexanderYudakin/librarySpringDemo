package com.example.library.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends EntityBase {

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "reader_uuid")
    private User reader;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();
}
