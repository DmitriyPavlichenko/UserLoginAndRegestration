package com.dimo.userloginandregistration.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private static final String NOT_FOUND_EXCEPTION_MSG = "User with %s email isn't exists";
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findAppUserByEmail(s)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND_EXCEPTION_MSG, s)));
    }

    public void singUpUser(AppUser user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException(user.getEmail() + " is already registered");
        }
        userRepository.save(user);
    }
}
