package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.exception.ResourceNotFoundException;
import com.orleansgo.conducteur.mapper.ConducteurMapper;
import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConducteurService {

    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;

    public ConducteurDTO creerConducteur(ConducteurDTO conducteurDTO) {
        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        return conducteurMapper.toDTO(conducteurRepository.save(conducteur));
    }

    public List<ConducteurDTO> listerTousLesConducteurs() {
        return conducteurRepository.findAll().stream()
                .map(conducteurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ConducteurDTO trouverParId(UUID id) {
        return conducteurRepository.findById(id)
                .map(conducteurMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé"));
    }

    public void supprimerConducteur(UUID id) {
        conducteurRepository.deleteById(id);
    }
}
package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.exception.ResourceNotFoundException;
import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConducteurService {

    private final ConducteurRepository conducteurRepository;

    @Transactional(readOnly = true)
    public List<ConducteurDTO> getAllConducteurs() {
        return conducteurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurById(UUID id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        return convertToDTO(conducteur);
    }

    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurByUtilisateurId(UUID utilisateurId) {
        Conducteur conducteur = conducteurRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé pour l'utilisateur ID: " + utilisateurId));
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO createConducteur(UUID utilisateurId) {
        if (conducteurRepository.findByUtilisateurId(utilisateurId).isPresent()) {
            throw new IllegalStateException("Un conducteur existe déjà pour cet utilisateur");
        }
        
        Conducteur conducteur = new Conducteur();
        conducteur.setUtilisateurId(utilisateurId);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO updateDisponibilite(UUID id, boolean disponible) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.setDisponible(disponible);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO ajouterGains(UUID id, BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.ajouterGains(montant);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO updateDocumentsValides(UUID id, boolean documentsValides) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.setDocumentsValides(documentsValides);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    private ConducteurDTO convertToDTO(Conducteur conducteur) {
        return new ConducteurDTO(
                conducteur.getId(),
                conducteur.getUtilisateurId(),
                conducteur.isDisponible(),
                conducteur.getNote(),
                conducteur.getTotalCourses(),
                conducteur.getGains(),
                conducteur.isDocumentsValides()
        );
    }
}
package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.dto.DocumentDTO;
import com.orleansgo.conducteur.dto.VehiculeConducteurDTO;
import com.orleansgo.conducteur.exception.ConducteurNotFoundException;
import com.orleansgo.conducteur.exception.DocumentNotFoundException;
import com.orleansgo.conducteur.exception.VehiculeNotFoundException;
import com.orleansgo.conducteur.model.*;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import com.orleansgo.conducteur.repository.DocumentRepository;
import com.orleansgo.conducteur.repository.VehiculeConducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConducteurService {
    
    private final ConducteurRepository conducteurRepository;
    private final DocumentRepository documentRepository;
    private final VehiculeConducteurRepository vehiculeConducteurRepository;
    
    @Transactional(readOnly = true)
    public List<ConducteurDTO> getAllConducteurs() {
        return conducteurRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurById(Long id) {
        return conducteurRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurByUtilisateurId(Long utilisateurId) {
        return conducteurRepository.findByUtilisateurId(utilisateurId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé pour l'utilisateur: " + utilisateurId));
    }
    
    @Transactional
    public ConducteurDTO createConducteur(ConducteurDTO conducteurDTO) {
        Conducteur conducteur = mapToEntity(conducteurDTO);
        conducteur.setDateCreation(LocalDateTime.now());
        conducteur.setDateModification(LocalDateTime.now());
        conducteur.setStatut(StatutConducteur.EN_ATTENTE_VALIDATION);
        conducteur.setNombreCourses(0);
        conducteur.setNombreAvis(0);
        conducteur.setNoteGlobale(0.0);
        
        Conducteur savedConducteur = conducteurRepository.save(conducteur);
        return mapToDTO(savedConducteur);
    }
    
    @Transactional
    public ConducteurDTO updateConducteur(Long id, ConducteurDTO conducteurDTO) {
        return conducteurRepository.findById(id)
                .map(conducteur -> {
                    if (conducteurDTO.getNumeroPermis() != null) {
                        conducteur.setNumeroPermis(conducteurDTO.getNumeroPermis());
                    }
                    if (conducteurDTO.getDateExpirationPermis() != null) {
                        conducteur.setDateExpirationPermis(conducteurDTO.getDateExpirationPermis());
                    }
                    conducteur.setDisponible(conducteurDTO.isDisponible());
                    if (conducteurDTO.getStatut() != null) {
                        conducteur.setStatut(conducteurDTO.getStatut());
                    }
                    if (conducteurDTO.getLatitude() != null && conducteurDTO.getLongitude() != null) {
                        conducteur.setLatitude(conducteurDTO.getLatitude());
                        conducteur.setLongitude(conducteurDTO.getLongitude());
                        conducteur.setDernierePosition(LocalDateTime.now());
                    }
                    conducteur.setDateModification(LocalDateTime.now());
                    
                    return mapToDTO(conducteurRepository.save(conducteur));
                })
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public ConducteurDTO updateStatut(Long id, StatutConducteur statut) {
        return conducteurRepository.findById(id)
                .map(conducteur -> {
                    conducteur.setStatut(statut);
                    conducteur.setDateModification(LocalDateTime.now());
                    return mapToDTO(conducteurRepository.save(conducteur));
                })
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public ConducteurDTO updatePosition(Long id, Double latitude, Double longitude) {
        return conducteurRepository.findById(id)
                .map(conducteur -> {
                    conducteur.setLatitude(latitude);
                    conducteur.setLongitude(longitude);
                    conducteur.setDernierePosition(LocalDateTime.now());
                    conducteur.setDateModification(LocalDateTime.now());
                    return mapToDTO(conducteurRepository.save(conducteur));
                })
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public ConducteurDTO updateDisponibilite(Long id, boolean disponible) {
        return conducteurRepository.findById(id)
                .map(conducteur -> {
                    conducteur.setDisponible(disponible);
                    conducteur.setDateModification(LocalDateTime.now());
                    return mapToDTO(conducteurRepository.save(conducteur));
                })
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public DocumentDTO ajouterDocument(Long conducteurId, DocumentDTO documentDTO) {
        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        Document document = new Document();
        document.setConducteur(conducteur);
        document.setType(documentDTO.getType());
        document.setNomFichier(documentDTO.getNomFichier());
        document.setUrlFichier(documentDTO.getUrlFichier());
        document.setStatut(StatutDocument.EN_ATTENTE);
        document.setDateCreation(LocalDateTime.now());
        document.setDateModification(LocalDateTime.now());
        
        if (documentDTO.getDateExpiration() != null) {
            document.setDateExpiration(documentDTO.getDateExpiration());
        }
        
        Document savedDocument = documentRepository.save(document);
        return mapToDocumentDTO(savedDocument);
    }
    
    @Transactional
    public DocumentDTO validerDocument(Long documentId, boolean valide, String commentaire, Long administrateurId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé avec l'ID: " + documentId));
        
        document.setStatut(valide ? StatutDocument.VALIDE : StatutDocument.REJETE);
        document.setCommentaireValidation(commentaire);
        document.setValidePar(administrateurId);
        document.setDateValidation(LocalDateTime.now());
        document.setDateModification(LocalDateTime.now());
        
        Document savedDocument = documentRepository.save(document);
        
        // Vérifier si tous les documents requis sont validés
        verifierValiditeDocumentsConducteur(document.getConducteur().getId());
        
        return mapToDocumentDTO(savedDocument);
    }
    
    @Transactional
    public VehiculeConducteurDTO ajouterVehicule(Long conducteurId, VehiculeConducteurDTO vehiculeDTO) {
        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        // Si le véhicule est marqué comme principal, désactiver les autres véhicules principaux
        if (vehiculeDTO.isVehiculePrincipal()) {
            vehiculeConducteurRepository.findByConducteurIdAndVehiculePrincipalTrue(conducteurId)
                    .ifPresent(v -> {
                        v.setVehiculePrincipal(false);
                        vehiculeConducteurRepository.save(v);
                    });
        }
        
        VehiculeConducteur vehicule = new VehiculeConducteur();
        vehicule.setConducteur(conducteur);
        vehicule.setVehiculeId(vehiculeDTO.getVehiculeId());
        vehicule.setVehiculePrincipal(vehiculeDTO.isVehiculePrincipal());
        vehicule.setDateDebut(LocalDateTime.now());
        vehicule.setStatut(StatutVehiculeConducteur.ACTIF);
        vehicule.setDateCreation(LocalDateTime.now());
        vehicule.setDateModification(LocalDateTime.now());
        
        VehiculeConducteur savedVehicule = vehiculeConducteurRepository.save(vehicule);
        return mapToVehiculeDTO(savedVehicule);
    }
    
    @Transactional
    public VehiculeConducteurDTO changerStatutVehicule(Long vehiculeId, StatutVehiculeConducteur statut) {
        VehiculeConducteur vehicule = vehiculeConducteurRepository.findById(vehiculeId)
                .orElseThrow(() -> new VehiculeNotFoundException("Véhicule conducteur non trouvé avec l'ID: " + vehiculeId));
        
        vehicule.setStatut(statut);
        if (statut == StatutVehiculeConducteur.SUPPRIME) {
            vehicule.setDateFin(LocalDateTime.now());
        }
        vehicule.setDateModification(LocalDateTime.now());
        
        VehiculeConducteur savedVehicule = vehiculeConducteurRepository.save(vehicule);
        return mapToVehiculeDTO(savedVehicule);
    }
    
    @Transactional
    public VehiculeConducteurDTO definirVehiculePrincipal(Long conducteurId, Long vehiculeId) {
        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        // Désactiver le véhicule principal actuel
        vehiculeConducteurRepository.findByConducteurIdAndVehiculePrincipalTrue(conducteurId)
                .ifPresent(v -> {
                    v.setVehiculePrincipal(false);
                    vehiculeConducteurRepository.save(v);
                });
        
        // Définir le nouveau véhicule principal
        VehiculeConducteur vehicule = vehiculeConducteurRepository.findById(vehiculeId)
                .orElseThrow(() -> new VehiculeNotFoundException("Véhicule conducteur non trouvé avec l'ID: " + vehiculeId));
        
        if (!vehicule.getConducteur().getId().equals(conducteurId)) {
            throw new IllegalArgumentException("Ce véhicule n'appartient pas au conducteur spécifié");
        }
        
        vehicule.setVehiculePrincipal(true);
        vehicule.setDateModification(LocalDateTime.now());
        
        VehiculeConducteur savedVehicule = vehiculeConducteurRepository.save(vehicule);
        return mapToVehiculeDTO(savedVehicule);
    }
    
    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByConducteurId(Long conducteurId) {
        conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        return documentRepository.findByConducteurId(conducteurId).stream()
                .map(this::mapToDocumentDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VehiculeConducteurDTO> getVehiculesByConducteurId(Long conducteurId) {
        conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        return vehiculeConducteurRepository.findByConducteurId(conducteurId).stream()
                .map(this::mapToVehiculeDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ConducteurDTO> getNearbyAvailableDrivers(Double latitude, Double longitude, Double distanceKm) {
        return conducteurRepository.findNearbyAvailableDrivers(latitude, longitude, distanceKm).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ConducteurDTO> getAvailableDrivers() {
        return conducteurRepository.findByDisponibleTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void verifierValiditeDocumentsConducteur(Long conducteurId) {
        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ConducteurNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
        
        // Liste des types de documents requis
        List<TypeDocument> documentsRequis = List.of(
                TypeDocument.PERMIS_CONDUIRE,
                TypeDocument.CARTE_IDENTITE,
                TypeDocument.ASSURANCE
        );
        
        // Vérifier si tous les documents requis sont validés
        boolean tousDocumentsValides = true;
        for (TypeDocument typeDocument : documentsRequis) {
            List<Document> documents = documentRepository.findByConducteurIdAndType(conducteurId, typeDocument);
            boolean documentTypeValide = documents.stream()
                    .anyMatch(doc -> doc.getStatut() == StatutDocument.VALIDE && 
                             (doc.getDateExpiration() == null || doc.getDateExpiration().isAfter(LocalDateTime.now())));
            
            if (!documentTypeValide) {
                tousDocumentsValides = false;
                break;
            }
        }
        
        conducteur.setDocumentsValides(tousDocumentsValides);
        
        // Si tous les documents sont validés et le conducteur est en attente, l'activer
        if (tousDocumentsValides && conducteur.getStatut() == StatutConducteur.EN_ATTENTE_VALIDATION) {
            conducteur.setStatut(StatutConducteur.ACTIF);
        } else if (!tousDocumentsValides && conducteur.getStatut() == StatutConducteur.ACTIF) {
            // Si les documents ne sont plus valides, suspendre le conducteur
            conducteur.setStatut(StatutConducteur.SUSPENDU);
        }
        
        conducteur.setDateModification(LocalDateTime.now());
        conducteurRepository.save(conducteur);
    }
    
    private ConducteurDTO mapToDTO(Conducteur conducteur) {
        Set<DocumentDTO> documents = conducteur.getDocuments().stream()
                .map(this::mapToDocumentDTO)
                .collect(Collectors.toSet());
        
        Set<VehiculeConducteurDTO> vehicules = conducteur.getVehicules().stream()
                .map(this::mapToVehiculeDTO)
                .collect(Collectors.toSet());
        
        return ConducteurDTO.builder()
                .id(conducteur.getId())
                .utilisateurId(conducteur.getUtilisateurId())
                .numeroPermis(conducteur.getNumeroPermis())
                .dateExpirationPermis(conducteur.getDateExpirationPermis())
                .documentsValides(conducteur.isDocumentsValides())
                .disponible(conducteur.isDisponible())
                .statut(conducteur.getStatut())
                .latitude(conducteur.getLatitude())
                .longitude(conducteur.getLongitude())
                .dernierePosition(conducteur.getDernierePosition())
                .noteGlobale(conducteur.getNoteGlobale())
                .nombreCourses(conducteur.getNombreCourses())
                .nombreAvis(conducteur.getNombreAvis())
                .documents(documents)
                .vehicules(vehicules)
                .build();
    }
    
    private Conducteur mapToEntity(ConducteurDTO dto) {
        return Conducteur.builder()
                .id(dto.getId())
                .utilisateurId(dto.getUtilisateurId())
                .numeroPermis(dto.getNumeroPermis())
                .dateExpirationPermis(dto.getDateExpirationPermis())
                .documentsValides(dto.isDocumentsValides())
                .disponible(dto.isDisponible())
                .statut(dto.getStatut())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .dernierePosition(dto.getDernierePosition())
                .noteGlobale(dto.getNoteGlobale())
                .nombreCourses(dto.getNombreCourses())
                .nombreAvis(dto.getNombreAvis())
                .build();
    }
    
    private DocumentDTO mapToDocumentDTO(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .conducteurId(document.getConducteur().getId())
                .type(document.getType())
                .nomFichier(document.getNomFichier())
                .urlFichier(document.getUrlFichier())
                .statut(document.getStatut())
                .commentaireValidation(document.getCommentaireValidation())
                .validePar(document.getValidePar())
                .dateValidation(document.getDateValidation())
                .dateExpiration(document.getDateExpiration())
                .build();
    }
    
    private VehiculeConducteurDTO mapToVehiculeDTO(VehiculeConducteur vehicule) {
        return VehiculeConducteurDTO.builder()
                .id(vehicule.getId())
                .conducteurId(vehicule.getConducteur().getId())
                .vehiculeId(vehicule.getVehiculeId())
                .vehiculePrincipal(vehicule.isVehiculePrincipal())
                .dateDebut(vehicule.getDateDebut())
                .dateFin(vehicule.getDateFin())
                .statut(vehicule.getStatut())
                .build();
    }
}
