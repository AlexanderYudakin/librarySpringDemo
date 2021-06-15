package com.example.library.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class NameEmbeddable {
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String middleName;
}
