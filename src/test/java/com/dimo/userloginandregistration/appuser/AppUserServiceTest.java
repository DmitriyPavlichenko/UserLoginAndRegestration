package com.dimo.userloginandregistration.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock private AppUserRepository repository;
    private AppUserService service;

    @BeforeEach
    void setUp() {
        service = new AppUserService(repository);
    }

    @Test
    void testLoadUserByUsername() {
        // given
        String email = "smith@gmail.com";
        AppUser user =
                new AppUser("Ann", "Smith", email, "password", UserRole.User);
        given(repository.findAppUserByEmail(email)).willReturn(Optional.of(user));

        // when
        String username = service.loadUserByUsername(email).getUsername();

        // then
        assertThat(username).isEqualTo(user.getEmail());
    }

    @Test
    void testSingUpNewUser() {
        // given
        AppUser user =
                new AppUser("Ann", "Smith", "smith@gmail.com", "password", UserRole.User);

        // when
        service.singUpUser(user);

        // then
        ArgumentCaptor<AppUser> userArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(userArgumentCaptor.capture());
        AppUser captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue).isEqualTo(user);
    }

    @Test
    void testSingUpUserThatAlreadyExists() {
        // given
        AppUser user =
                new AppUser("Ann", "Smith", "smith@gmail.com", "password", UserRole.User);
        given(repository.existsByEmail(anyString())).willReturn(true);

        // then
        assertThatThrownBy(() -> service.singUpUser(user))
                .isInstanceOf(IllegalStateException.class);
        verify(repository, never()).save(user);
    }

}