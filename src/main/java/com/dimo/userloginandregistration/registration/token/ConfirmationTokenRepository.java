package com.dimo.userloginandregistration.registration.token;

import com.dimo.userloginandregistration.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> getConfirmationTokenByToken(String token);
    Optional<ConfirmationToken> getConfirmationTokenByUser_Email(String email);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken t " +
            "SET t.expiresAt = ?2 " +
            "WHERE t.token = ?1")
    int updateExpiresAt(String token, LocalDateTime expiresAt);
}
