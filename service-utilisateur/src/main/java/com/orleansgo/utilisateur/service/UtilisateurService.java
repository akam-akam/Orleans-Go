package com.orleansgo.utilisateur.service;

import com.orleansgo.utilisateur.dto.UtilisateurDTO;
import com.orleansgo.utilisateur.exception.ResourceAlreadyExistsException;
import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.mapper.UtilisateurMapper;
import com.orleansgo.utilisateur.model.Utilisateur;
import com.orleansgo.utilisateur.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurDTO creerUtilisateur(UtilisateurDTO utilisateurDTO) {
        // Vérification de l'unicité de l'email
        utilisateurRepository.findByEmail(utilisateurDTO.getEmail())
                .ifPresent(u -> {
                    throw new ResourceAlreadyExistsException("Email déjà utilisé");
                });

        // Vérification de l'unicité du numéro de téléphone (si nécessaire)
        if (utilisateurDTO.getNumeroTelephone() != null) {
            utilisateurRepository.findByNumeroTelephone(utilisateurDTO.getNumeroTelephone())
                    .ifPresent(u -> {
                        throw new ResourceAlreadyExistsException("Numéro de téléphone déjà utilisé");
                    });
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setCodeParrainage(genererCodeParrainageUnique());
        utilisateur.setActif(true);
        utilisateur.setEmailVerifie(false);
        utilisateur.setTelephoneVerifie(false);

        return utilisateurMapper.toDTO(utilisateurRepository.save(utilisateur));
    }

    public Optional<UtilisateurDTO> trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .map(utilisateurMapper::toDTO);
    }

    public List<UtilisateurDTO> listerTousLesUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO mettreAJourUtilisateur(UUID id, UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        utilisateurMapper.updateUtilisateurFromDTO(utilisateurDTO, utilisateur);
        if (utilisateurDTO.getMotDePasse() != null) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

        return utilisateurMapper.toDTO(utilisateurRepository.save(utilisateur));
    }

    public void supprimerUtilisateur(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        utilisateurRepository.delete(utilisateur);
    }

    public void activerUtilisateur(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        utilisateur.setActif(true);
        utilisateurRepository.save(utilisateur);
    }

    public void desactiverUtilisateur(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
    }

    public void verifierEmail(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        utilisateur.setEmailVerifie(true);
        utilisateurRepository.save(utilisateur);
    }

    public void verifierTelephone(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        utilisateur.setTelephoneVerifie(true);
        utilisateurRepository.save(utilisateur);
    }

    private String genererCodeParrainageUnique() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (utilisateurRepository.existsByCodeParrainage(code));
        return code;
    }
}
