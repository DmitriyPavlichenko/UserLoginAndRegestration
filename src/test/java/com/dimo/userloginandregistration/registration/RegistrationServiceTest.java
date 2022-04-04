package com.dimo.userloginandregistration.registration;

import com.dimo.userloginandregistration.appuser.AppUser;
import com.dimo.userloginandregistration.appuser.AppUserService;
import com.dimo.userloginandregistration.appuser.UserRole;
import com.dimo.userloginandregistration.email.EmailService;
import com.dimo.userloginandregistration.registration.token.ConfirmationToken;
import com.dimo.userloginandregistration.registration.token.ConfirmationTokenService;
import com.dimo.userloginandregistration.security.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    AppUserService userService;
    @Mock
    ConfirmationTokenService tokenService;
    @Mock
    EmailService emailService;
    @Mock
    EmailValidator emailValidator;
    @Mock
    PasswordEncoder passwordEncoder;
    RegistrationService service;

    @BeforeEach
    void setUp() {
        service = new RegistrationService(userService, emailValidator, passwordEncoder, tokenService, emailService);
    }

    @Test
    void canRegister() {
        // given
        RegistrationRequest request =
                new RegistrationRequest("firstName", "lastName", "email", "password");
        given(!emailValidator.test(anyString())).willReturn(true);

        // when
        String result = service.register(request);

        // then
        assertThat(result).isEqualTo("Successfully registered");
    }

    @Test
    void cannotRegister() {
        // given
        RegistrationRequest request =
                new RegistrationRequest("firstName", "lastName", "email", "password");
        given(!emailValidator.test(anyString())).willReturn(false);

        // when
        Throwable throwable = catchThrowable(() -> service.register(request));

        // then
        assertThat(throwable).isInstanceOf(IllegalStateException.class).hasMessage("Invalid email address");
    }

    @Test
    void testResentConfirmationToken() {
        String email = "email";
        given(tokenService.getTokenWithExpiresDate(email))
                .willReturn(new ConfirmationToken("token", LocalDateTime.now(), LocalDateTime.now(), new AppUser(
                        "firstName", "lastName", "email", "password", UserRole.Admin)));

        // when
        service.resentConfirmationToken(email);

        // then
        verify(emailService).sendEmail(anyString(), anyString());
    }

    @Test
    void testConfirmToken() {
        // when
        service.confirmToken(anyString());

        // then
        verify(tokenService).confirmToken(anyString());
    }
}
