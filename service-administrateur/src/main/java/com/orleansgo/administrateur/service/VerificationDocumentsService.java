
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.VerificationDocumentsDTO;
import com.orleansgo.administrateur.exception.AdministrateurNotFoundException;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.StatutVerification;
import com.orleansgo.administrateur.model.VerificationDocuments;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.VerificationDocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificationDocumentsService {
    
    private final VerificationDocumentsRepository verificationDocumentsRepository;
    private final AdministrateurRepository administrateurRepository;
    
    public List<VerificationDocumentsDTO> getAllVerifications() {
        return verificationDocumentsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<VerificationDocumentsDTO> getVerificationsByChauffeur(String chauffeurId) {
        return verificationDocumentsRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<VerificationDocumentsDTO> getVerificationsByStatut(StatutVerification statut) {
        return verificationDocumentsRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public VerificationDocumentsDTO getVerification(String id) {
        return verificationDocumentsRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vérification non trouvée avec l'ID: " + id));
    }
    
    @Transactional
    public VerificationDocumentsDTO createVerification(VerificationDocumentsDTO verificationDTO) {
        Administrateur administrateur = null;
        if (verificationDTO.getAdministrateurId() != null) {
            administrateur = administrateurRepository.findById(verificationDTO.getAdministrateurId())
                    .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + verificationDTO.getAdministrateurId()));
        }
        
        VerificationDocuments verification = new VerificationDocuments();
        verification.setChauffeurId(verificationDTO.getChauffeurId());
        verification.setPermisValide(verificationDTO.isPermisValide());
        verification.setCarteIdentiteValide(verificationDTO.isCarteIdentiteValide());
        verification.setAssuranceValide(verificationDTO.isAssuranceValide());
        verification.setCarteTechniqueValide(verificationDTO.isCarteTechniqueValide());
        verification.setCommentaires(verificationDTO.getCommentaires());
        verification.setAdministrateur(administrateur);
        verification.setDateVerification(LocalDateTime.now());
        verification.setStatut(verificationDTO.getStatut());
        
        return convertToDTO(verificationDocumentsRepository.save(verification));
    }
    
    @Transactional
    public VerificationDocumentsDTO updateVerification(String id, VerificationDocumentsDTO verificationDTO) {
        VerificationDocuments verification = verificationDocumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vérification non trouvée avec l'ID: " + id));
        
        Administrateur administrateur = null;
        if (verificationDTO.getAdministrateurId() != null) {
            administrateur = administrateurRepository.findById(verificationDTO.getAdministrateurId())
                    .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + verificationDTO.getAdministrateurId()));
        }
        
        verification.setPermisValide(verificationDTO.isPermisValide());
        verification.setCarteIdentiteValide(verificationDTO.isCarteIdentiteValide());
        verification.setAssuranceValide(verificationDTO.isAssuranceValide());
        verification.setCarteTechniqueValide(verificationDTO.isCarteTechniqueValide());
        verification.setCommentaires(verificationDTO.getCommentaires());
        verification.setAdministrateur(administrateur);
        verification.setStatut(verificationDTO.getStatut());
        
        return convertToDTO(verificationDocumentsRepository.save(verification));
    }
    
    @Transactional
    public VerificationDocumentsDTO updateStatut(String id, StatutVerification statut, String administrateurId, String commentaires) {
        VerificationDocuments verification = verificationDocumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vérification non trouvée avec l'ID: " + id));
        
        Administrateur administrateur = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + administrateurId));
        
        verification.setStatut(statut);
        verification.setAdministrateur(administrateur);
        verification.setDateVerification(LocalDateTime.now());
        
        if (commentaires != null && !commentaires.isEmpty()) {
            verification.setCommentaires(commentaires);
        }
        
        return convertToDTO(verificationDocumentsRepository.save(verification));
    }
    
    private VerificationDocumentsDTO convertToDTO(VerificationDocuments verification) {
        VerificationDocumentsDTO dto = new VerificationDocumentsDTO();
        dto.setId(verification.getId());
        dto.setChauffeurId(verification.getChauffeurId());
        dto.setPermisValide(verification.isPermisValide());
        dto.setCarteIdentiteValide(verification.isCarteIdentiteValide());
        dto.setAssuranceValide(verification.isAssuranceValide());
        dto.setCarteTechniqueValide(verification.isCarteTechniqueValide());
        dto.setCommentaires(verification.getCommentaires());
        dto.setAdministrateurId(verification.getAdministrateur() != null ? verification.getAdministrateur().getId() : null);
        dto.setDateVerification(verification.getDateVerification());
        dto.setStatut(verification.getStatut());
        return dto;
    }
}
