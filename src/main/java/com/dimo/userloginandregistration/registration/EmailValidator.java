package com.dimo.userloginandregistration.registration;

import com.dimo.userloginandregistration.appuser.AppUser;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String user) {
        return true;
    }
}
