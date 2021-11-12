package com.dimo.userloginandregistration.registration.token;

import com.dimo.userloginandregistration.appuser.AppUser;
import com.dimo.userloginandregistration.appuser.AppUserRepository;
import com.dimo.userloginandregistration.appuser.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private AppUserRepository userRepository;
    private ConfirmationToken givenConfirmationToken;

    @BeforeEach
    void setUp() {
        AppUser givenUser =
                new AppUser("Ann", "Smith", "email", "password", UserRole.User);
        userRepository.save(givenUser);
        givenConfirmationToken =
                new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now(), givenUser);
        tokenRepository.save(givenConfirmationToken);
    }

    @Test
    void testGetConfirmationTokenByToken() {
        // given
        String token = "token";

        // when
        ConfirmationToken expectedConfirmationToken = tokenRepository.getConfirmationTokenByToken(token)
                .orElseThrow(IllegalStateException::new);

        // then
        assertThat(expectedConfirmationToken).isEqualTo(givenConfirmationToken);
    }

    @Test
    void testGetConfirmationTokenByTokenThatDoesntExists() {
        // given
        String token = "invalid_token";

        // when
        ConfirmationToken expectedConfirmationToken = tokenRepository.getConfirmationTokenByToken(token)
                .orElse(null);

        // then
        assertThat(expectedConfirmationToken).isNull();
    }

    @Test
    void testGetConfirmationTokenByEmail() {
        // given
        String email = "email";

        // when
        ConfirmationToken expectedConfirmationToken = tokenRepository.getConfirmationTokenByUser_Email(email)
                .orElseThrow(IllegalStateException::new);

        // then
        assertThat(expectedConfirmationToken).isEqualTo(givenConfirmationToken);
    }

    @Test
    void testGetConfirmationTokenByEmailThatDoesntExists() {
        // given
        String email = "invalid_email";

        // when
        ConfirmationToken expectedConfirmationToken = tokenRepository.getConfirmationTokenByUser_Email(email)
                .orElse(null);

        // then
        assertThat(expectedConfirmationToken).isNull();
    }

    @Test
    void testUpdateConfirmedAt() {
        // when
        tokenRepository.updateConfirmedAt("token", LocalDateTime.parse("2021-11-10T22:43:10.773"));

        // then
        assertThat(tokenRepository.getConfirmationTokenByToken("token").get().getConfirmedAt())
                .isEqualTo(LocalDateTime.parse("2021-11-10T22:43:10.773"));
    }

    @Test
    void updateExpiresAt() {
        // when
        tokenRepository.updateExpiresAt("token", LocalDateTime.parse("2021-11-10T22:43:10.773"));

        // then
        assertThat(tokenRepository.getConfirmationTokenByToken("token").get().getExpiresAt())
                .isEqualTo(LocalDateTime.parse("2021-11-10T22:43:10.773"));
    }
}