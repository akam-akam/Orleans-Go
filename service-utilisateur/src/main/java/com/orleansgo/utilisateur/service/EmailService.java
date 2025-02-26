package com.orleansgo.utilisateur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String email, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        // Implémentation réelle d'envoi d'email
    }
}
