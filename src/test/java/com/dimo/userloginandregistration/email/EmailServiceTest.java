package com.dimo.userloginandregistration.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private Environment env;
    @Mock
    private JavaMailSender mailSender;
    private EmailService service;

    @BeforeEach
    void setUp() {
        service = new EmailService(env, mailSender);
    }

    @Test
    void canSendEmail() {
        // given
        String to = "to";
        String message = "message";
        given(mailSender.createMimeMessage()).willAnswer(invocationOnMock -> new MimeMessage(any(Session.class)));
        given(env.getProperty(anyString())).willReturn("from@mail");

        // when
        String result = service.sendEmail(to, message);

        // then
        verify(mailSender).createMimeMessage();
        assertThat(result).isEqualTo("Confirmation email was sent");
    }

    @Test
    void cannotSendEmail() {
        // given
        String to = "to";
        String message = "message";
        given(mailSender.createMimeMessage()).willAnswer(invocationOnMock -> new MimeMessage(any(Session.class)));
        given(env.getProperty(anyString())).willReturn("");

        // when
        String result = service.sendEmail(to, message);

        // then
        assertThat(result).contains("Illegal address");
    }
}
