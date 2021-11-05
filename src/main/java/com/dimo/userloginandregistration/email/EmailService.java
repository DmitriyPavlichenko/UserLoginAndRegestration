package com.dimo.userloginandregistration.email;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Service
public class EmailService implements EmailSender{
    private Environment env;
    private JavaMailSender mailSender;

    @Override
    public String sendEmail(String to, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setText(message, true);
            messageHelper.setTo(to);
            messageHelper.setSubject("Email confirmation");
            messageHelper.setFrom(env.getProperty("spring.mail.username"));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            return e.getMessage();
        }
        return "Confirmation email was sent";
    }
}
