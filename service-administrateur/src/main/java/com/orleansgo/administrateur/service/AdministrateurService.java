package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.mapper.AdministrateurMapper;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final AdministrateurMapper administrateurMapper;

    public AdministrateurDTO creerAdministrateur(AdministrateurDTO administrateurDTO) {
        Administrateur administrateur = administrateurMapper.toEntity(administrateurDTO);
        return administrateurMapper.toDTO(administrateurRepository.save(administrateur));
    }

    public List<AdministrateurDTO> listerTousLesAdministrateurs() {
        return administrateurRepository.findAll().stream()
                .map(administrateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AdministrateurDTO trouverParId(UUID id) {
        return administrateurRepository.findById(id)
                .map(administrateurMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Administrateur non trouvé"));
    }

    public void supprimerAdministrateur(UUID id) {
        administrateurRepository.deleteById(id);
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.exception.AdministrateurNotFoundException;
import com.orleansgo.administrateur.exception.EmailAlreadyExistsException;
import com.orleansgo.administrateur.model.ActionUtilisateur;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.RoleAdministrateur;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrateurService {
    
    private final AdministrateurRepository administrateurRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<AdministrateurDTO> getAllAdministrateurs() {
        return administrateurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public AdministrateurDTO getAdministrateur(String id) {
        return administrateurRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public AdministrateurDTO createAdministrateur(AdministrateurDTO administrateurDTO, String motDePasse) {
        if (administrateurRepository.existsByEmail(administrateurDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Un administrateur avec cet email existe déjà");
        }
        
        Administrateur administrateur = new Administrateur();
        administrateur.setEmail(administrateurDTO.getEmail());
        administrateur.setMotDePasse(passwordEncoder.encode(motDePasse));
        administrateur.setNom(administrateurDTO.getNom());
        administrateur.setPrenom(administrateurDTO.getPrenom());
        administrateur.setRole(administrateurDTO.getRole());
        administrateur.setActif(true);
        administrateur.setDateCreation(LocalDateTime.now());
        
        return convertToDTO(administrateurRepository.save(administrateur));
    }
    
    @Transactional
    public AdministrateurDTO updateAdministrateur(String id, AdministrateurDTO administrateurDTO) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
        
        // Vérifier si l'email a changé et s'il existe déjà
        if (!administrateur.getEmail().equals(administrateurDTO.getEmail()) && 
                administrateurRepository.existsByEmail(administrateurDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Un administrateur avec cet email existe déjà");
        }
        
        administrateur.setEmail(administrateurDTO.getEmail());
        administrateur.setNom(administrateurDTO.getNom());
        administrateur.setPrenom(administrateurDTO.getPrenom());
        administrateur.setRole(administrateurDTO.getRole());
        administrateur.setActif(administrateurDTO.isActif());
        
        return convertToDTO(administrateurRepository.save(administrateur));
    }
    
    @Transactional
    public void deleteAdministrateur(String id) {
        if (!administrateurRepository.existsById(id)) {
            throw new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id);
        }
        administrateurRepository.deleteById(id);
    }
    
    @Transactional
    public AdministrateurDTO changeAdministrateurRole(String id, RoleAdministrateur newRole) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
        
        administrateur.setRole(newRole);
        return convertToDTO(administrateurRepository.save(administrateur));
    }
    
    @Transactional
    public AdministrateurDTO updateAdministrateurStatus(String id, boolean actif) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
        
        administrateur.setActif(actif);
        return convertToDTO(administrateurRepository.save(administrateur));
    }
    
    @Transactional
    public void resetPassword(String id, String newPassword) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
        
        administrateur.setMotDePasse(passwordEncoder.encode(newPassword));
        administrateurRepository.save(administrateur);
    }
    
    @Transactional
    public void updateDerniereConnexion(String id) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + id));
        
        administrateur.setDerniereConnexion(LocalDateTime.now());
        administrateurRepository.save(administrateur);
    }
    
    // Méthode pour appliquer une action sur un utilisateur (passager ou chauffeur)
    @Transactional
    public void gererUtilisateur(String utilisateurId, ActionUtilisateur action, String administrateurId) {
        // Vérification de l'existence de l'administrateur
        Administrateur administrateur = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + administrateurId));
        
        // Appel au service utilisateur ou chauffeur via Feign Client pour appliquer l'action
        // Ceci est une implémentation fictive, le code réel dépendrait de vos clients Feign
        switch (action) {
            case ACTIVER:
                // utilisateurClient.activerUtilisateur(utilisateurId);
                break;
            case DESACTIVER:
                // utilisateurClient.desactiverUtilisateur(utilisateurId);
                break;
            case SUSPENDRE:
                // utilisateurClient.suspendreUtilisateur(utilisateurId);
                break;
            case SUPPRIMER:
                // utilisateurClient.supprimerUtilisateur(utilisateurId);
                break;
            case MODIFIER_ROLE:
                // utilisateurClient.modifierRoleUtilisateur(utilisateurId, nouveauRole);
                break;
            case REINITIALISER_MOT_DE_PASSE:
                // utilisateurClient.reinitialiserMotDePasse(utilisateurId);
                break;
            default:
                throw new IllegalArgumentException("Action non supportée");
        }
    }
    
    private AdministrateurDTO convertToDTO(Administrateur administrateur) {
        AdministrateurDTO dto = new AdministrateurDTO();
        dto.setId(administrateur.getId());
        dto.setEmail(administrateur.getEmail());
        dto.setNom(administrateur.getNom());
        dto.setPrenom(administrateur.getPrenom());
        dto.setRole(administrateur.getRole());
        dto.setActif(administrateur.isActif());
        dto.setDerniereConnexion(administrateur.getDerniereConnexion());
        dto.setDateCreation(administrateur.getDateCreation());
        return dto;
    }
}
