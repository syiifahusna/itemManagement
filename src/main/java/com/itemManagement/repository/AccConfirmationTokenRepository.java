package com.itemManagement.repository;

import com.itemManagement.entity.AccConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccConfirmationTokenRepository extends JpaRepository<AccConfirmationToken,Long> {
    Optional<AccConfirmationToken> findByToken(String token);
}
