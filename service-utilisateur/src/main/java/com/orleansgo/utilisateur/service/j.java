package com.orleansgo.utilisateur.service;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TotpService {

    private final Base32 base32 = new Base32();

    public String generateSecret() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        return base32.encodeToString(bytes);
    }

    public boolean validateCode(String secret, String code) {
        try {
            int expectedCode = TimeBasedOneTimePasswordUtil.generateCurrentNumber(secret);
            int receivedCode = Integer.parseInt(code);
            return MessageDigest.isEqual(
                    String.valueOf(expectedCode).getBytes(),
                    String.valueOf(receivedCode).getBytes()
            );
        } catch (Exception e) {
            return false;
        }
    }

    public String generateQrCode(String email, String secret) {
        return "otpauth://totp/OrleansGO:" + email + "?secret=" + secret + "&issuer=OrleansGO";
    }

    public Set<String> generateBackupCodes() {
        return IntStream.range(0, 10)
                .mapToObj(i -> RandomStringUtils.randomNumeric(8))
                .collect(Collectors.toSet());
    }
}
