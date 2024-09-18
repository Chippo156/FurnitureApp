package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailAndGoogleAccountId(String email , int googleAccountId);
    Optional<User> findByEmailAndGithubAccountId(String email , int githubAccountId);
    Optional<User> findByEmailAndFacebookAccountId(String email , int facebookAccountId);
    @Query("SELECT u FROM User u WHERE :keyword is null or :keyword = '' or u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword% OR u.address LIKE %:keyword%")
    Page<User> getUsers(@Param("keyword") String keyword , Pageable pageable);


}
