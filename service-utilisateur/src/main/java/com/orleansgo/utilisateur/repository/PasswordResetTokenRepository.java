package com.orleansgo.utilisateur.repository;


import com.orleansgo.utilisateur.model.PasswordResetToken;
import com.orleansgo.utilisateur.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUtilisateur(Utilisateur utilisateur);
}
