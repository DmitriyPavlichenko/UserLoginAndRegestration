package com.dimo.userloginandregistration.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE AppUser u " +
            "SET u.enabled = ?2 " +
            "WHERE u.email = ?1")
    int updateEnabledByEmail(String email, boolean enabled);
}
