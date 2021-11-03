package com.dimo.userloginandregistration.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {
    RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String submitToken(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
