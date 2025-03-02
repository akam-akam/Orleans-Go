
package com.orleansgo.utilisateur.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TwoFactorService {

    // Pour un systèlme complet, nous utiliserons une bibliothèque comme GoogleAuth
    // Cette implémentation est une version simplifiée pour démonstration
    
    public String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public boolean validateCode(String secret, String code) {
        // Dans une implémentation réelle, on comparerait avec le code TOTP généré
        // Pour cette démo, on accepte n'importe quel code à 6 chiffres
        return code != null && code.matches("\\d{6}");
    }
    
    public String getQrCodeUrl(String secret, String email) {
        // Dans une implémentation réelle, on retournerait une URL pour générer un QR code
        return "otpauth://totp/OrleansGO:" + email + "?secret=" + secret + "&issuer=OrleansGO";
    }
}
