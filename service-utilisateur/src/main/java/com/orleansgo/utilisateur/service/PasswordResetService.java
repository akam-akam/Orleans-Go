package com.orleansgo.utilisateur.service;

import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.model.PasswordResetToken;
import com.orleansgo.utilisateur.model.Utilisateur;
import com.orleansgo.utilisateur.repository.PasswordResetTokenRepository;
import com.orleansgo.utilisateur.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void demanderReinitialisationMotDePasse(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUtilisateur(utilisateur);
        token.setDateExpiration(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(token);

        // Envoyer l'email avec le token (implémentation à faire)
    }

    @Transactional
    public void reinitialiserMotDePasse(String token, String nouveauMotDePasse) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token invalide"));

        if (resetToken.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expiré");
        }

        Utilisateur utilisateur = resetToken.getUtilisateur();
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);

        passwordResetTokenRepository.delete(resetToken);
    }
}
