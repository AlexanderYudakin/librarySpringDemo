package com.example.library.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class EntityBase {
    @Id
    @Column(columnDefinition = "varchar(36)")
    private UUID id = UUID.randomUUID();
}
