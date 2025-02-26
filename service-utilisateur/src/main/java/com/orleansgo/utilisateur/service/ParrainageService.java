package com.orleansgo.utilisateur.service;

import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.model.Utilisateur;
import com.orleansgo.utilisateur.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParrainageService {

    private final UtilisateurRepository utilisateurRepository;
    private static final BigDecimal MONTANT_PARRAINAGE = new BigDecimal("5.00");

    @Transactional
    public void appliquerParrainage(UUID utilisateurId, String codeParrainage) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Utilisateur parrain = utilisateurRepository.findByCodeParrainage(codeParrainage)
                .orElseThrow(() -> new ResourceNotFoundException("Code de parrainage invalide"));

        // Ajouter le bonus au parrain
        parrain.setSoldeBonus(parrain.getSoldeBonus().add(MONTANT_PARRAINAGE));
        utilisateurRepository.save(parrain);

        // Ajouter le bonus au filleul
        utilisateur.setSoldeBonus(utilisateur.getSoldeBonus().add(MONTANT_PARRAINAGE));
        utilisateurRepository.save(utilisateur);
    }
}
