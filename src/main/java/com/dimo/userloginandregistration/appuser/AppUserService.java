package com.dimo.userloginandregistration.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findAppUserByEmail(s)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + s + " email isn't exists"));
    }

    public void singUpUser(AppUser user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException(user.getEmail() + " is already registered");
        }
        userRepository.save(user);
    }
}
