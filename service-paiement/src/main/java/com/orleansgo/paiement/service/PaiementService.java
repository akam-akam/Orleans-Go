package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.enumeration.MethodePaiement;
import com.orleansgo.paiement.enumeration.StatutPaiement;
import com.orleansgo.paiement.exception.PaiementException;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public PaiementService(PaiementRepository paiementRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.paiementRepository = paiementRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public PaiementDTO creerPaiement(PaiementDTO paiementDTO) {
        // Validation des données
        if (paiementDTO.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaiementException("Le montant du paiement doit être supérieur à zéro");
        }

        if (paiementDTO.getTrajetId() == null) {
            throw new PaiementException("L'ID du trajet est obligatoire");
        }

        if (paiementDTO.getMethodePaiement() == null) {
            throw new PaiementException("La méthode de paiement est obligatoire");
        }

        // Création du paiement
        Paiement paiement = new Paiement();
        paiement.setTrajetId(paiementDTO.getTrajetId());
        paiement.setPassagerId(paiementDTO.getPassagerId());
        paiement.setChauffeurId(paiementDTO.getChauffeurId());
        paiement.setMontant(paiementDTO.getMontant());
        paiement.setMethodePaiement(paiementDTO.getMethodePaiement());
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        paiement.setDateCreation(LocalDateTime.now());
        paiement.setReference(genererReference());

        paiement = paiementRepository.save(paiement);

        // Notification de création de paiement via Kafka
        Map<String, Object> notification = new HashMap<>();
        notification.put("action", "PAIEMENT_CREE");
        notification.put("paiementId", paiement.getId().toString());
        notification.put("trajetId", paiement.getTrajetId().toString());
        notification.put("montant", paiement.getMontant());
        notification.put("reference", paiement.getReference());

        kafkaTemplate.send("paiements", notification);

        return convertToDTO(paiement);
    }

    @Transactional
    public PaiementDTO traiterPaiement(UUID paiementId, String referenceTransaction) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new PaiementException("Paiement non trouvé avec l'ID: " + paiementId));

        if (paiement.getStatut() != StatutPaiement.EN_ATTENTE) {
            throw new PaiementException("Ce paiement a déjà été traité");
        }

        // Simulation de traitement selon la méthode de paiement
        boolean paiementReussi = simulerPaiement(paiement.getMethodePaiement(), paiement.getMontant());

        if (paiementReussi) {
            paiement.setStatut(StatutPaiement.COMPLETE);
            paiement.setReferenceTransaction(referenceTransaction);
            paiement.setDateValidation(LocalDateTime.now());

            // Notification de paiement réussi
            Map<String, Object> notification = new HashMap<>();
            notification.put("action", "PAIEMENT_REUSSI");
            notification.put("paiementId", paiement.getId().toString());
            notification.put("trajetId", paiement.getTrajetId().toString());
            notification.put("montant", paiement.getMontant());
            notification.put("reference", paiement.getReference());

            kafkaTemplate.send("paiements", notification);
        } else {
            paiement.setStatut(StatutPaiement.ECHOUE);

            // Notification d'échec de paiement
            Map<String, Object> notification = new HashMap<>();
            notification.put("action", "PAIEMENT_ECHOUE");
            notification.put("paiementId", paiement.getId().toString());
            notification.put("trajetId", paiement.getTrajetId().toString());
            notification.put("montant", paiement.getMontant());
            notification.put("reference", paiement.getReference());

            kafkaTemplate.send("paiements", notification);
        }

        paiement = paiementRepository.save(paiement);
        return convertToDTO(paiement);
    }

    @Transactional
    public PaiementDTO annulerPaiement(UUID paiementId, String raison) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new PaiementException("Paiement non trouvé avec l'ID: " + paiementId));

        if (paiement.getStatut() == StatutPaiement.COMPLETE) {
            throw new PaiementException("Impossible d'annuler un paiement déjà complété");
        }

        paiement.setStatut(StatutPaiement.ANNULE);
        paiement.setCommentaires(raison);

        // Notification d'annulation de paiement
        Map<String, Object> notification = new HashMap<>();
        notification.put("action", "PAIEMENT_ANNULE");
        notification.put("paiementId", paiement.getId().toString());
        notification.put("trajetId", paiement.getTrajetId().toString());
        notification.put("raison", raison);

        kafkaTemplate.send("paiements", notification);

        paiement = paiementRepository.save(paiement);
        return convertToDTO(paiement);
    }

    @Transactional(readOnly = true)
    public PaiementDTO getPaiementById(UUID id) {
        return paiementRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new PaiementException("Paiement non trouvé avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> getPaiementsByTrajet(UUID trajetId) {
        return paiementRepository.findByTrajetId(trajetId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> getPaiementsByPassager(UUID passagerId) {
        return paiementRepository.findByPassagerId(passagerId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> getPaiementsByChauffeur(UUID chauffeurId) {
        return paiementRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Méthode pour simuler le traitement d'un paiement
    private boolean simulerPaiement(MethodePaiement methode, BigDecimal montant) {
        // Dans un environnement réel, cette méthode ferait appel à un service de paiement tiers
        // Pour l'exemple, on simule une réussite dans 90% des cas
        Random random = new Random();
        return random.nextDouble() <= 0.9;
    }

    // Génération d'une référence unique pour le paiement
    private String genererReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Conversion Entité vers DTO
    private PaiementDTO convertToDTO(Paiement paiement) {
        PaiementDTO dto = new PaiementDTO();
        dto.setId(paiement.getId());
        dto.setTrajetId(paiement.getTrajetId());
        dto.setPassagerId(paiement.getPassagerId());
        dto.setChauffeurId(paiement.getChauffeurId());
        dto.setMontant(paiement.getMontant());
        dto.setMethodePaiement(paiement.getMethodePaiement());
        dto.setStatut(paiement.getStatut());
        dto.setReference(paiement.getReference());
        dto.setReferenceTransaction(paiement.getReferenceTransaction());
        dto.setCommentaires(paiement.getCommentaires());
        dto.setDateCreation(paiement.getDateCreation());
        dto.setDateValidation(paiement.getDateValidation());
        return dto;
    }
}