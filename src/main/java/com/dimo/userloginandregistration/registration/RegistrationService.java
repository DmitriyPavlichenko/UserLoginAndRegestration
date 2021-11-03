package com.dimo.userloginandregistration.registration;

import com.dimo.userloginandregistration.appuser.AppUser;
import com.dimo.userloginandregistration.appuser.AppUserService;
import com.dimo.userloginandregistration.appuser.UserRole;
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

    public String register(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail())) {
            throw new IllegalStateException("Invalid email address");
        }

        AppUser user = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), UserRole.User);
        userService.singUpUser(user);

        UUID uuid = UUID.randomUUID();
        ConfirmationToken token = new ConfirmationToken(uuid.toString(), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20), user);
        tokenService.saveConfirmationToken(token);

        return "Successfully registered";
    }

    public String confirmToken(String token) {
        return tokenService.confirmToken(token);
    }
}
