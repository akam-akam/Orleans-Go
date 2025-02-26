package com.orleansgo.utilisateur.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
@EnableScheduling
@Slf4j
public class JwtKeyRotationConfig {

    private final AtomicReference<SecretKey> currentKey = new AtomicReference<>(generateNewKey());
    private final AtomicReference<Date> nextRotation = new AtomicReference<>(calculateNextRotation());

    @Bean
    public SecretKey currentJwtKey() {
        return currentKey.get();
    }

    @Scheduled(fixedRateString = "${jwt.key-rotation.interval:86400000}")
    public void rotateKey() {
        log.info("Rotating JWT signing key");
        currentKey.set(generateNewKey());
        nextRotation.set(calculateNextRotation());
        log.info("New JWT key generated. Next rotation at {}", nextRotation.get());
    }

    private SecretKey generateNewKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private Date calculateNextRotation() {
        return new Date(System.currentTimeMillis() + 86400000); // 24 hours
    }
}
