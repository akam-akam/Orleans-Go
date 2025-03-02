package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.exception.ResourceNotFoundException;
import com.orleansgo.paiement.mapper.PaiementMapper;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    public PaiementDTO creerPaiement(PaiementDTO paiementDTO) {
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        return paiementMapper.toDTO(paiementRepository.save(paiement));
    }

    public List<PaiementDTO> listerTousLesPaiements() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaiementDTO trouverParId(UUID id) {
        return paiementRepository.findById(id)
                .map(paiementMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));
    }

    public void supprimerPaiement(UUID id) {
        paiementRepository.deleteById(id);
    }
}
package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.dto.TransactionDTO;
import com.orleansgo.paiement.exception.ResourceNotFoundException;
import com.orleansgo.paiement.model.MethodePaiement;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.model.StatutPaiement;
import com.orleansgo.paiement.model.Transaction;
import com.orleansgo.paiement.repository.PaiementRepository;
import com.orleansgo.paiement.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final TransactionRepository transactionRepository;
    private final CommissionService commissionService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<PaiementDTO> getAllPaiements() {
        return paiementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PaiementDTO getPaiementById(UUID id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + id));
        return mapToDTO(paiement);
    }

    public List<PaiementDTO> getPaiementsByUserId(UUID userId) {
        return paiementRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PaiementDTO> getPaiementsByTrajetId(UUID trajetId) {
        return paiementRepository.findByTrajetId(trajetId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaiementDTO initierPaiement(PaiementDTO paiementDTO) {
        Paiement paiement = Paiement.builder()
                .userId(paiementDTO.getUserId())
                .trajetId(paiementDTO.getTrajetId())
                .montant(paiementDTO.getMontant())
                .methode(paiementDTO.getMethode())
                .statut(StatutPaiement.EN_ATTENTE)
                .dateCreation(LocalDateTime.now())
                .build();
        
        Paiement savedPaiement = paiementRepository.save(paiement);
        
        // Publier un événement pour le traitement asynchrone
        kafkaTemplate.send("paiements-initiations", savedPaiement.getId().toString(), savedPaiement);
        
        return mapToDTO(savedPaiement);
    }

    @Transactional
    public PaiementDTO confirmerPaiement(UUID paiementId, String referenceTransaction) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + paiementId));
        
        paiement.setStatut(StatutPaiement.COMPLETE);
        paiement.setReferenceTransaction(referenceTransaction);
        paiement.setDatePaiement(LocalDateTime.now());
        
        Paiement updatedPaiement = paiementRepository.save(paiement);
        
        // Calculer et enregistrer la commission
        BigDecimal montantCommission = commissionService.calculerCommission(paiement.getMontant());
        
        // Enregistrer la transaction pour le chauffeur
        Transaction transactionChauffeur = Transaction.builder()
                .paiementId(paiementId)
                .userId(paiement.getChauffeurId())
                .montant(paiement.getMontant().subtract(montantCommission))
                .type("CREDIT")
                .description("Paiement pour course #" + paiement.getTrajetId())
                .dateTransaction(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transactionChauffeur);
        
        // Enregistrer la commission pour la plateforme
        Transaction transactionCommission = Transaction.builder()
                .paiementId(paiementId)
                .userId(null) // Plateforme
                .montant(montantCommission)
                .type("COMMISSION")
                .description("Commission sur course #" + paiement.getTrajetId())
                .dateTransaction(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transactionCommission);
        
        // Publier un événement pour la notification
        kafkaTemplate.send("paiements-confirmations", updatedPaiement.getId().toString(), updatedPaiement);
        
        return mapToDTO(updatedPaiement);
    }

    @Transactional
    public PaiementDTO annulerPaiement(UUID paiementId, String raisonAnnulation) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + paiementId));
        
        paiement.setStatut(StatutPaiement.ANNULE);
        paiement.setCommentaire(raisonAnnulation);
        paiement.setDateMiseAJour(LocalDateTime.now());
        
        Paiement updatedPaiement = paiementRepository.save(paiement);
        
        // Publier un événement pour la notification
        kafkaTemplate.send("paiements-annulations", updatedPaiement.getId().toString(), updatedPaiement);
        
        return mapToDTO(updatedPaiement);
    }

    private PaiementDTO mapToDTO(Paiement paiement) {
        return PaiementDTO.builder()
                .id(paiement.getId())
                .userId(paiement.getUserId())
                .chauffeurId(paiement.getChauffeurId())
                .trajetId(paiement.getTrajetId())
                .montant(paiement.getMontant())
                .methode(paiement.getMethode())
                .statut(paiement.getStatut())
                .referenceTransaction(paiement.getReferenceTransaction())
                .commentaire(paiement.getCommentaire())
                .dateCreation(paiement.getDateCreation())
                .datePaiement(paiement.getDatePaiement())
                .dateMiseAJour(paiement.getDateMiseAJour())
                .build();
    }
}
