package com.dimo.userloginandregistration.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @Mock
    private RegistrationService service;
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        controller = new RegistrationController(service);
    }

    @Test
    void canRegister() {
        // given
        RegistrationRequest request = new RegistrationRequest("firstName", "lastName", "email", "password");

        // when
        controller.register(request);

        // then
        verify(service).register(request);
    }

    @Test
    void canSubmitToken() {
        // given
        String token = "token";

        // when
        controller.submitToken(token);

        // then
        verify(service).confirmToken(token);
    }

    @Test
    void canResentConfirmationToken() {
        // given
        String email = "email";

        // when
        controller.resentConfirmationToken(email);

        // then
        verify(service).resentConfirmationToken(email);
    }
}
