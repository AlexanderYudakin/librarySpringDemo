package com.example.library.repository;

import com.example.library.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Set<Book> findByTitle(String title);
}
