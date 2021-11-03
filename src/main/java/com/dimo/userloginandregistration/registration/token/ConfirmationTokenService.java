package com.dimo.userloginandregistration.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private ConfirmationTokenRepository tokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }
    public String confirmToken(String token) {
        if (tokenRepository.getConfirmationTokenByToken(token).isPresent()) {
            ConfirmationToken confirmationToken = tokenRepository.getConfirmationTokenByToken(token).get();
            if (confirmationToken.getCreatedAt().isBefore(confirmationToken.getExpiresAt())) {
                confirmationToken.setConfirmedAt(LocalDateTime.now());
                confirmationToken.getUser().setLocked(false);
                return "Email successfully confirmed";
            }
        }
        return "Email wasn't confirmed";
    }
}
