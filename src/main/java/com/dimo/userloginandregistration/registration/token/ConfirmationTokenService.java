package com.dimo.userloginandregistration.registration.token;

import com.dimo.userloginandregistration.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private ConfirmationTokenRepository tokenRepository;
    private AppUserRepository userRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }

    public String confirmToken(String token) {
        if (tokenRepository.getConfirmationTokenByToken(token).isPresent()) {
            ConfirmationToken confirmationToken = tokenRepository.getConfirmationTokenByToken(token).get();
            if (confirmationToken.getCreatedAt().isBefore(confirmationToken.getExpiresAt())) {
                tokenRepository.updateConfirmedAt(confirmationToken.getToken(), LocalDateTime.now());
                userRepository.updateEnabledByEmail(confirmationToken.getUser().getEmail(), true);
                return "Email successfully confirmed";
            }
        }
        return "Email wasn't confirmed";
    }

    public ConfirmationToken getTokenWithExpiresDate(String email) {
        ConfirmationToken confirmationToken =
                tokenRepository.getConfirmationTokenByUser_Email(email)
                        .orElseThrow(() -> new IllegalStateException("Invalid email"));
        tokenRepository.updateExpiresAt(confirmationToken.getToken(), LocalDateTime.now().plusMinutes(20));
        return confirmationToken;
    }
}
