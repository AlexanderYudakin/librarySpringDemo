package com.example.library.repository;

import com.example.library.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Query("SELECT author FROM Author author WHERE author.nameEmbeddable.firstName=:firstName AND author.nameEmbeddable.lastName=:lastName AND author.nameEmbeddable.middleName=:middleName")
    Set<Author> findByFirstLastMiddleName(String firstName, String lastName, String middleName);
}
