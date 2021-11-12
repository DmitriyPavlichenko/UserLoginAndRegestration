package com.dimo.userloginandregistration.registration.token;

import com.dimo.userloginandregistration.appuser.AppUser;
import com.dimo.userloginandregistration.appuser.AppUserRepository;
import com.dimo.userloginandregistration.appuser.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {
    @Mock
    private ConfirmationTokenRepository tokenRepository;
    @Mock
    private AppUserRepository userRepository;
    private ConfirmationTokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new ConfirmationTokenService(tokenRepository, userRepository);
    }

    @Test
    void saveConfirmationToken() {
        // given
        ConfirmationToken confirmationToken =
                new ConfirmationToken("token",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new AppUser("Ann", "Smith", "smith@gmail.com", "password", UserRole.User)
                );

        // when
        tokenService.saveConfirmationToken(confirmationToken);

        // then
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    void testConfirmTokenSuccessfully() {
        // given
        String token = "token";
        ConfirmationToken confirmationToken =
                new ConfirmationToken("token",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        new AppUser("Ann", "Smith", "smith@gmail.com", "password", UserRole.User)
                );
        confirmationToken.setConfirmedAt(LocalDateTime.MIN);
        confirmationToken.setExpiresAt(LocalDateTime.MAX);
        given(tokenRepository.getConfirmationTokenByToken(token)).willReturn(Optional.of(confirmationToken));

        // when
        String confirmToken = tokenService.confirmToken(token);

        // then
        verify(tokenRepository).updateConfirmedAt(anyString(), any(LocalDateTime.class));
        verify(userRepository).updateEnabledByEmail(anyString(), anyBoolean());
        assertThat(confirmToken).isEqualTo("Email successfully confirmed");
    }

    @Test
    void testConfirmTokenUnsuccessfully() {
        // when
        String confirmToken = tokenService.confirmToken(anyString());

        // then
        assertThat(confirmToken).isEqualTo("Email wasn't confirmed");
    }


    @Test
    void testGetTokenWithExpiresDate() {
        // given
        String email = "email";
        String token = "token";
        ConfirmationToken confirmationToken = new ConfirmationToken("token",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new AppUser("Ann", "Smith", "smith@gmail.com", "password", UserRole.User)
        );
        given(tokenRepository.getConfirmationTokenByUser_Email(email)).willReturn(Optional.of(confirmationToken));

        // when
        ConfirmationToken tokenWithExpiresDate = tokenService.getTokenWithExpiresDate(email);

        // then
        ArgumentCaptor<LocalDateTime> argumentCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(tokenRepository).updateExpiresAt(any(), argumentCaptor.capture());
        LocalDateTime captorValue = argumentCaptor.getValue();
        assertThat(tokenWithExpiresDate.getExpiresAt().plusMinutes(20))
                .isEqualToIgnoringSeconds(captorValue);
    }
}