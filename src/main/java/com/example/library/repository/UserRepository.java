package com.example.library.repository;

import com.example.library.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT user FROM User user WHERE user.nameEmbeddable.firstName=:firstName AND user.nameEmbeddable.lastName=:lastName AND user.nameEmbeddable.middleName=:middleName")
    Set<User> findByFirstLastMiddleName(String firstName, String lastName, String middleName);
    Optional<User> findByEmail(String email);
}
