package com.dimo.userloginandregistration.registration;

import com.dimo.userloginandregistration.appuser.AppUser;
import com.dimo.userloginandregistration.appuser.AppUserService;
import com.dimo.userloginandregistration.appuser.UserRole;
import com.dimo.userloginandregistration.email.EmailService;
import com.dimo.userloginandregistration.registration.token.ConfirmationToken;
import com.dimo.userloginandregistration.registration.token.ConfirmationTokenService;
import com.dimo.userloginandregistration.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {
    private AppUserService userService;
    private EmailValidator emailValidator;
    private PasswordEncoder passwordEncoder;
    private ConfirmationTokenService tokenService;
    private EmailService emailService;

    public String register(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail())) {
            throw new IllegalStateException("Invalid email address");
        }

        AppUser user = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), UserRole.User);
        user.setEnabled(false);
        userService.singUpUser(user);

        UUID uuid = UUID.randomUUID();
        ConfirmationToken token = new ConfirmationToken(uuid.toString(), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20), user);
        tokenService.saveConfirmationToken(token);

        sendMail(user.getEmail(), user.getFirstName(), token.getToken());

        return "Successfully registered";
    }

    public String resentConfirmationToken(String email) {
        ConfirmationToken token = tokenService.getTokenWithExspiresDate(email);
        return sendMail(token.getUser().getEmail(), token.getUser().getFirstName(), token.getToken());
    }

    private String sendMail(String emailAddress, String firstName, String token) {
        return emailService.sendEmail(emailAddress, buildEmail(firstName,
                "https://localhost:8080/api/v1/registration/confirm?token=" + token));
    }

    public String confirmToken(String token) {
        return tokenService.confirmToken(token);
    }

    private String buildEmail(String name, String link) {
        return "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "
                + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " +
                "Thank you for registering. Please click on the below link to activate your account: " +
                "</p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;" +
                "padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;" +
                "font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
                + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>";
    }
}
