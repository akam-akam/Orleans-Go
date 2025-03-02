
package com.orleansgo.evenement.service;

import com.orleansgo.evenement.dto.InscriptionDTO;
import com.orleansgo.evenement.model.Evenement;
import com.orleansgo.evenement.model.Inscription;
import com.orleansgo.evenement.model.StatutEvenement;
import com.orleansgo.evenement.model.StatutInscription;
import com.orleansgo.evenement.repository.EvenementRepository;
import com.orleansgo.evenement.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EvenementRepository evenementRepository;

    public List<InscriptionDTO> getAllInscriptions() {
        return inscriptionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InscriptionDTO getInscriptionById(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'ID: " + id));
        return convertToDto(inscription);
    }

    @Transactional
    public InscriptionDTO createInscription(InscriptionDTO inscriptionDTO) {
        // Vérifier si l'événement existe
        Evenement evenement = evenementRepository.findById(inscriptionDTO.getEvenementId())
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID: " + inscriptionDTO.getEvenementId()));
        
        // Vérifier si l'événement accepte les inscriptions
        if (!evenement.isNecessiteInscription()) {
            throw new RuntimeException("Cet événement n'exige pas d'inscription");
        }
        
        // Vérifier si l'événement est toujours en cours ou planifié
        if (evenement.getStatut() == StatutEvenement.TERMINE || evenement.getStatut() == StatutEvenement.ANNULE) {
            throw new RuntimeException("Impossible de s'inscrire à un événement terminé ou annulé");
        }
        
        // Vérifier si l'utilisateur est déjà inscrit
        Optional<Inscription> existingInscription = inscriptionRepository.findByEvenementIdAndUtilisateurId(
                inscriptionDTO.getEvenementId(), inscriptionDTO.getUtilisateurId());
        
        if (existingInscription.isPresent()) {
            throw new RuntimeException("Utilisateur déjà inscrit à cet événement");
        }
        
        // Vérifier s'il reste des places disponibles
        if (evenement.getCapaciteMax() != null && evenement.getNombreInscrits() >= evenement.getCapaciteMax()) {
            throw new RuntimeException("Événement complet, plus de places disponibles");
        }
        
        Inscription inscription = new Inscription();
        inscription.setEvenement(evenement);
        inscription.setUtilisateurId(inscriptionDTO.getUtilisateurId());
        inscription.setDateInscription(LocalDateTime.now());
        inscription.setStatut(StatutInscription.EN_ATTENTE);
        inscription.setPaiementEffectue(false);
        
        // Si l'événement est gratuit, confirmer automatiquement l'inscription
        if (evenement.getTarifInscription() == 0) {
            inscription.setStatut(StatutInscription.CONFIRMEE);
            inscription.setDateConfirmation(LocalDateTime.now());
            inscription.setPaiementEffectue(true);
            
            // Mettre à jour le nombre d'inscrits de l'événement
            evenement.setNombreInscrits(evenement.getNombreInscrits() + 1);
            evenementRepository.save(evenement);
        }
        
        Inscription savedInscription = inscriptionRepository.save(inscription);
        return convertToDto(savedInscription);
    }

    @Transactional
    public InscriptionDTO updateStatutInscription(Long id, StatutInscription statut) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'ID: " + id));
        
        StatutInscription oldStatut = inscription.getStatut();
        inscription.setStatut(statut);
        
        if (statut == StatutInscription.CONFIRMEE) {
            inscription.setDateConfirmation(LocalDateTime.now());
            
            // Si l'inscription est confirmée et n'était pas déjà confirmée
            if (oldStatut != StatutInscription.CONFIRMEE) {
                Evenement evenement = inscription.getEvenement();
                evenement.setNombreInscrits(evenement.getNombreInscrits() + 1);
                evenementRepository.save(evenement);
            }
        } else if (statut == StatutInscription.ANNULEE || statut == StatutInscription.REFUSEE) {
            inscription.setDateAnnulation(LocalDateTime.now());
            
            // Si l'inscription est annulée ou refusée et était confirmée auparavant
            if (oldStatut == StatutInscription.CONFIRMEE) {
                Evenement evenement = inscription.getEvenement();
                evenement.setNombreInscrits(evenement.getNombreInscrits() - 1);
                evenementRepository.save(evenement);
            }
        }
        
        Inscription updatedInscription = inscriptionRepository.save(inscription);
        return convertToDto(updatedInscription);
    }

    @Transactional
    public InscriptionDTO confirmerPaiement(Long id, String referenceTransaction) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'ID: " + id));
        
        inscription.setPaiementEffectue(true);
        inscription.setReferenceTransaction(referenceTransaction);
        
        // Si l'inscription n'était pas déjà confirmée, la confirmer maintenant
        if (inscription.getStatut() != StatutInscription.CONFIRMEE) {
            inscription.setStatut(StatutInscription.CONFIRMEE);
            inscription.setDateConfirmation(LocalDateTime.now());
            
            // Mettre à jour le nombre d'inscrits de l'événement
            Evenement evenement = inscription.getEvenement();
            evenement.setNombreInscrits(evenement.getNombreInscrits() + 1);
            evenementRepository.save(evenement);
        }
        
        Inscription updatedInscription = inscriptionRepository.save(inscription);
        return convertToDto(updatedInscription);
    }

    @Transactional
    public void deleteInscription(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'ID: " + id));
        
        // Si l'inscription était confirmée, mettre à jour le nombre d'inscrits de l'événement
        if (inscription.getStatut() == StatutInscription.CONFIRMEE) {
            Evenement evenement = inscription.getEvenement();
            evenement.setNombreInscrits(evenement.getNombreInscrits() - 1);
            evenementRepository.save(evenement);
        }
        
        inscriptionRepository.delete(inscription);
    }

    public List<InscriptionDTO> getInscriptionsByEvenement(Long evenementId) {
        return inscriptionRepository.findByEvenementId(evenementId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InscriptionDTO> getInscriptionsByUtilisateur(Long utilisateurId) {
        return inscriptionRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InscriptionDTO> getInscriptionsByStatut(StatutInscription statut) {
        return inscriptionRepository.findByStatut(statut).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InscriptionDTO> getInscriptionsByUtilisateurAndStatut(Long utilisateurId, StatutInscription statut) {
        return inscriptionRepository.findByUtilisateurIdAndStatut(utilisateurId, statut).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InscriptionDTO> getInscriptionsByEvenementAndStatut(Long evenementId, StatutInscription statut) {
        return inscriptionRepository.findByEvenementIdAndStatut(evenementId, statut).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InscriptionDTO convertToDto(Inscription inscription) {
        InscriptionDTO dto = new InscriptionDTO();
        dto.setId(inscription.getId());
        dto.setEvenementId(inscription.getEvenement().getId());
        dto.setUtilisateurId(inscription.getUtilisateurId());
        dto.setTitreEvenement(inscription.getEvenement().getTitre());
        dto.setDateDebut(inscription.getEvenement().getDateDebut());
        dto.setDateFin(inscription.getEvenement().getDateFin());
        dto.setDateInscription(inscription.getDateInscription());
        dto.setStatut(inscription.getStatut());
        dto.setDateConfirmation(inscription.getDateConfirmation());
        dto.setDateAnnulation(inscription.getDateAnnulation());
        dto.setCommentaire(inscription.getCommentaire());
        dto.setPaiementEffectue(inscription.isPaiementEffectue());
        dto.setReferenceTransaction(inscription.getReferenceTransaction());
        dto.setTarifInscription(inscription.getEvenement().getTarifInscription());
        return dto;
    }
}
