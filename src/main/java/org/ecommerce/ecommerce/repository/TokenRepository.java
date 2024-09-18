package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Token;
import org.ecommerce.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.userId = :user")
    List<Token> findByUser(@Param("user") User user);

    @Query("SELECT t FROM Token t WHERE t.refreshToken = :refreshToken")
    Token findByRefreshToken(String refreshToken);

}
