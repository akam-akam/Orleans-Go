package com.orleansgo.utilisateur.repository;

import com.orleansgo.utilisateur.model.Utilisateur;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNumeroTelephone(String numeroTelephone);
    boolean existsByCodeParrainage(String codeParrainage);

    Optional<Utilisateur> findByNumeroTelephone(@Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Format de numéro invalide") String numeroTelephone);
}
