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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificationDocumentsService {

    private final VerificationDocumentsRepository verificationDocumentsRepository;
    private final AdministrateurRepository administrateurRepository;

    public List<VerificationDocumentsDTO> getAllVerifications() {
        return verificationDocumentsRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public VerificationDocumentsDTO getVerificationById(UUID id) {
        VerificationDocuments verification = verificationDocumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vérification de documents non trouvée avec l'ID: " + id));
        return mapToDTO(verification);
    }

    public List<VerificationDocumentsDTO> getVerificationsByChauffeurId(UUID chauffeurId) {
        return verificationDocumentsRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VerificationDocumentsDTO> getVerificationsEnAttente() {
        return verificationDocumentsRepository.findByStatut(StatutVerification.EN_ATTENTE).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VerificationDocumentsDTO createVerification(VerificationDocumentsDTO verificationDTO) {
        VerificationDocuments verification = VerificationDocuments.builder()
                .id(UUID.randomUUID())
                .chauffeurId(verificationDTO.getChauffeurId())
                .permisValide(false)
                .carteGriseValide(false)
                .assuranceValide(false)
                .statutVerification(StatutVerification.EN_ATTENTE)
                .dateCreation(LocalDateTime.now())
                .build();

        VerificationDocuments savedVerification = verificationDocumentsRepository.save(verification);
        return mapToDTO(savedVerification);
    }

    @Transactional
    public VerificationDocumentsDTO updateVerification(UUID id, VerificationDocumentsDTO verificationDTO, UUID administrateurId) {
        Administrateur administrateur = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + administrateurId));

        VerificationDocuments verification = verificationDocumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vérification de documents non trouvée avec l'ID: " + id));

        verification.setPermisValide(verificationDTO.isPermisValide());
        verification.setCarteGriseValide(verificationDTO.isCarteGriseValide());
        verification.setAssuranceValide(verificationDTO.isAssuranceValide());
        verification.setCommentaires(verificationDTO.getCommentaires());
        verification.setAdministrateurId(administrateurId);
        verification.setDateVerification(LocalDateTime.now());

        // Déterminer le statut en fonction de la validation de tous les documents
        boolean tousDocumentsValides = verification.isPermisValide() &&
                verification.isCarteGriseValide() &&
                verification.isAssuranceValide();

        verification.setStatutVerification(tousDocumentsValides ?
                StatutVerification.VALIDE :
                StatutVerification.REJETE);

        VerificationDocuments updatedVerification = verificationDocumentsRepository.save(verification);
        return mapToDTO(updatedVerification);
    }

    private VerificationDocumentsDTO mapToDTO(VerificationDocuments verification) {
        return VerificationDocumentsDTO.builder()
                .id(verification.getId())
                .chauffeurId(verification.getChauffeurId())
                .permisValide(verification.isPermisValide())
                .carteGriseValide(verification.isCarteGriseValide())
                .assuranceValide(verification.isAssuranceValide())
                .statutVerification(verification.getStatutVerification())
                .commentaires(verification.getCommentaires())
                .administrateurId(verification.getAdministrateurId())
                .dateCreation(verification.getDateCreation())
                .dateVerification(verification.getDateVerification())
                .build();
    }
}