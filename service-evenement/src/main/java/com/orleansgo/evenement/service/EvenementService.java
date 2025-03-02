
package com.orleansgo.evenement.service;

import com.orleansgo.evenement.dto.EvenementDTO;
import com.orleansgo.evenement.model.Evenement;
import com.orleansgo.evenement.model.StatutEvenement;
import com.orleansgo.evenement.repository.EvenementRepository;
import com.orleansgo.evenement.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final InscriptionRepository inscriptionRepository;

    public List<EvenementDTO> getAllEvenements() {
        return evenementRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EvenementDTO getEvenementById(Long id) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID: " + id));
        return convertToDto(evenement);
    }

    @Transactional
    public EvenementDTO createEvenement(EvenementDTO evenementDTO) {
        Evenement evenement = convertToEntity(evenementDTO);
        evenement.setDateCreation(LocalDateTime.now());
        evenement.setStatut(StatutEvenement.PLANIFIE);
        Evenement savedEvenement = evenementRepository.save(evenement);
        return convertToDto(savedEvenement);
    }

    @Transactional
    public EvenementDTO updateEvenement(Long id, EvenementDTO evenementDTO) {
        Evenement existingEvenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID: " + id));
        
        if (evenementDTO.getTitre() != null) {
            existingEvenement.setTitre(evenementDTO.getTitre());
        }
        if (evenementDTO.getDescription() != null) {
            existingEvenement.setDescription(evenementDTO.getDescription());
        }
        if (evenementDTO.getDateDebut() != null) {
            existingEvenement.setDateDebut(evenementDTO.getDateDebut());
        }
        if (evenementDTO.getDateFin() != null) {
            existingEvenement.setDateFin(evenementDTO.getDateFin());
        }
        if (evenementDTO.getLieu() != null) {
            existingEvenement.setLieu(evenementDTO.getLieu());
        }
        if (evenementDTO.getImage() != null) {
            existingEvenement.setImage(evenementDTO.getImage());
        }
        if (evenementDTO.getCapaciteMax() != null) {
            existingEvenement.setCapaciteMax(evenementDTO.getCapaciteMax());
        }
        if (evenementDTO.getStatut() != null) {
            existingEvenement.setStatut(evenementDTO.getStatut());
        }
        existingEvenement.setPublicationActive(evenementDTO.isPublicationActive());
        if (evenementDTO.getCategories() != null) {
            existingEvenement.setCategories(evenementDTO.getCategories());
        }
        if (evenementDTO.getLatitude() != null) {
            existingEvenement.setLatitude(evenementDTO.getLatitude());
        }
        if (evenementDTO.getLongitude() != null) {
            existingEvenement.setLongitude(evenementDTO.getLongitude());
        }
        if (evenementDTO.getReglesParticipation() != null) {
            existingEvenement.setReglesParticipation(evenementDTO.getReglesParticipation());
        }
        if (evenementDTO.getTarifInscription() != null) {
            existingEvenement.setTarifInscription(evenementDTO.getTarifInscription());
        }
        existingEvenement.setNecessiteInscription(evenementDTO.isNecessiteInscription());
        
        existingEvenement.setDateModification(LocalDateTime.now());
        
        Evenement updatedEvenement = evenementRepository.save(existingEvenement);
        return convertToDto(updatedEvenement);
    }

    @Transactional
    public void deleteEvenement(Long id) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID: " + id));
        
        // Vérifier si l'événement a des inscriptions
        if (inscriptionRepository.countConfirmedInscriptionsByEvenementId(id) > 0 && 
            evenement.getStatut() != StatutEvenement.TERMINE) {
            // Si l'événement a des inscriptions, on le marque comme annulé plutôt que de le supprimer
            evenement.setStatut(StatutEvenement.ANNULE);
            evenement.setDateModification(LocalDateTime.now());
            evenementRepository.save(evenement);
        } else {
            // Sinon on peut le supprimer
            evenementRepository.delete(evenement);
        }
    }

    public List<EvenementDTO> getEvenementsByStatut(StatutEvenement statut) {
        return evenementRepository.findByStatut(statut).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvenementDTO> getEvenementsPublics() {
        return evenementRepository.findByPublicationActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvenementDTO> getEvenementsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return evenementRepository.findByPeriode(debut, fin).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvenementDTO> getEvenementsByCreateur(Long createurId) {
        return evenementRepository.findByCreateurId(createurId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvenementDTO> getEvenementsByCategorie(String categorie) {
        return evenementRepository.findByCategorie(categorie).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<EvenementDTO> searchEvenements(String titre, Pageable pageable) {
        return evenementRepository.findByTitreContainingIgnoreCase(titre, pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public EvenementDTO changerStatutEvenement(Long id, StatutEvenement statut) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID: " + id));
        
        evenement.setStatut(statut);
        evenement.setDateModification(LocalDateTime.now());
        
        Evenement updatedEvenement = evenementRepository.save(evenement);
        return convertToDto(updatedEvenement);
    }

    @Transactional
    public void updateEventsStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        // Mettre à jour les événements qui doivent passer à "EN_COURS"
        List<Evenement> eventsToDeploy = evenementRepository.findEventsToDeploy(now);
        for (Evenement event : eventsToDeploy) {
            event.setStatut(StatutEvenement.EN_COURS);
            event.setDateModification(now);
        }
        
        // Mettre à jour les événements qui doivent passer à "TERMINE"
        List<Evenement> eventsToClose = evenementRepository.findEventsToClose(now);
        for (Evenement event : eventsToClose) {
            event.setStatut(StatutEvenement.TERMINE);
            event.setDateModification(now);
        }
        
        evenementRepository.saveAll(eventsToDeploy);
        evenementRepository.saveAll(eventsToClose);
    }

    private EvenementDTO convertToDto(Evenement evenement) {
        EvenementDTO dto = new EvenementDTO();
        dto.setId(evenement.getId());
        dto.setTitre(evenement.getTitre());
        dto.setDescription(evenement.getDescription());
        dto.setDateDebut(evenement.getDateDebut());
        dto.setDateFin(evenement.getDateFin());
        dto.setLieu(evenement.getLieu());
        dto.setImage(evenement.getImage());
        dto.setCapaciteMax(evenement.getCapaciteMax());
        dto.setNombreInscrits(evenement.getNombreInscrits());
        dto.setStatut(evenement.getStatut());
        dto.setPublicationActive(evenement.isPublicationActive());
        dto.setCategories(evenement.getCategories());
        dto.setLatitude(evenement.getLatitude());
        dto.setLongitude(evenement.getLongitude());
        dto.setDateCreation(evenement.getDateCreation());
        dto.setDateModification(evenement.getDateModification());
        dto.setCreateurId(evenement.getCreateurId());
        dto.setReglesParticipation(evenement.getReglesParticipation());
        dto.setTarifInscription(evenement.getTarifInscription());
        dto.setNecessiteInscription(evenement.isNecessiteInscription());
        return dto;
    }

    private Evenement convertToEntity(EvenementDTO dto) {
        Evenement evenement = new Evenement();
        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setDateDebut(dto.getDateDebut());
        evenement.setDateFin(dto.getDateFin());
        evenement.setLieu(dto.getLieu());
        evenement.setImage(dto.getImage());
        evenement.setCapaciteMax(dto.getCapaciteMax());
        evenement.setNombreInscrits(0); // Nouvelle création, 0 inscrit
        evenement.setStatut(StatutEvenement.PLANIFIE); // Par défaut
        evenement.setPublicationActive(dto.isPublicationActive());
        evenement.setCategories(dto.getCategories());
        evenement.setLatitude(dto.getLatitude());
        evenement.setLongitude(dto.getLongitude());
        evenement.setCreateurId(dto.getCreateurId());
        evenement.setReglesParticipation(dto.getReglesParticipation());
        evenement.setTarifInscription(dto.getTarifInscription() != null ? dto.getTarifInscription() : 0.0);
        evenement.setNecessiteInscription(dto.isNecessiteInscription());
        return evenement;
    }
}
