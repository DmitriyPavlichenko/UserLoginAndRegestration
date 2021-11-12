package com.dimo.userloginandregistration.appuser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppUserRepositoryTest {
    @Autowired
    AppUserRepository repository;

    @Test
    void testIfUserFindsByEmail() {
        // giving
        String email = "smith@gmail.com";
        AppUser givenUser =
                new AppUser("Ann", "Smith", email, "password", UserRole.User);
        repository.save(givenUser);

        // when
        AppUser user = repository.findAppUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Invalid email"));

        // then
        assertThat(user.equals(givenUser)).isTrue();
    }

    @Test
    void testIfUserDoesntFindsByEmail() {
        // giving
        String email = "smith@gmail.com";

        // when
        AppUser user = repository.findAppUserByEmail(email)
                .orElse(null);

        // then
        assertThat(user).isNull();
    }


    @Test
    void testIfUserExistsByEmail() {
        // giving
        String email = "smith@gmail.com";
        AppUser givenUser =
                new AppUser("Ann", "Smith", email, "password", UserRole.User);
        repository.save(givenUser);

        // when
        boolean isUserExists = repository.existsByEmail(email);

        // then
        assertThat(isUserExists).isTrue();
    }

    @Test
    void testIfUserDoesntExistsByEmail() {
        // giving
        String email = "smith@gmail.com";

        // when
        boolean isUserExists = repository.existsByEmail(email);

        // then
        assertThat(isUserExists).isFalse();
    }

    @Test
    void testIfUserUnableIsTrue() {
        // giving
        String email = "smith@gmail.com";
        AppUser givenUser =
                new AppUser("Ann", "Smith", email, "password", UserRole.User);
        givenUser.setEnabled(false);
        repository.save(givenUser);

        // when
        int executionCode = repository.updateEnabledByEmail(email, true);
        AppUser user = repository.findAppUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email isn't found"));
        boolean expected = user.isEnabled();

        // then
        assertThat(executionCode).isEqualTo(1);
        assertThat(expected).isTrue();
    }

    @Test
    void testIfUserUnableIsFalse() {
        // giving
        String email = "smith@gmail.com";
        AppUser givenUser =
                new AppUser("Ann", "Smith", email, "password", UserRole.User);
        givenUser.setEnabled(true);
        repository.save(givenUser);

        // when
        int executionCode = repository.updateEnabledByEmail(email, false);
        AppUser user = repository.findAppUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email isn't found"));
        boolean expected = user.isEnabled();

        // then
        assertThat(executionCode).isEqualTo(1);
        assertThat(expected).isFalse();
    }
}