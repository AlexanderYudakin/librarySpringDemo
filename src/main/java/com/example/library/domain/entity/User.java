package com.example.library.domain.entity;

import com.example.library.domain.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends EntityBase {

    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "userType")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    private NameEmbeddable nameEmbeddable;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();;
}
