package com.dimo.userloginandregistration.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock private Environment env;
    @Mock private JavaMailSender mailSender;
    private EmailService service;

    @BeforeEach
    void setUp() {
        service = new EmailService(env, mailSender);
    }

    @Test
    @Disabled
    void sendEmail() {
        // given
        String email = "pabafi4049@funboxcn.com";


        // when
        service.sendEmail(email, "All is work!");
/*
        // then
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));*/
    }
}