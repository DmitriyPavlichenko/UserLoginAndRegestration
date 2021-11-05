package com.dimo.userloginandregistration.email;

public interface EmailSender {
    String sendEmail(String to, String message);
}
