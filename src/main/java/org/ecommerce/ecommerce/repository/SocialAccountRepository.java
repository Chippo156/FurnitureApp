package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.SocialAccount;
import org.ecommerce.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    boolean existsByProviderId(String providerId);

    Optional<SocialAccount> findByProviderId(String providerId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM SocialAccount s WHERE s.user.id = :userId")
    boolean findByUserId(@Param("userId") Long userId);
}
