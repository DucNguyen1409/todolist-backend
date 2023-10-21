package com.example.demo.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("FROM Token t inner join User u ON t.user.id = u.id " +
            " WHERE u.id = :userId " +
            " AND (t.expired = FALSE or t.revoked = FALSE ) ")
    List<Token> findAllValidTokensByUser(String userId);

    Optional<Token> findByToken(String token);
}
