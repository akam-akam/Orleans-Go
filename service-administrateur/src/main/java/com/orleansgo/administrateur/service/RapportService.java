
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.RapportDTO;
import com.orleansgo.administrateur.exception.AdministrateurNotFoundException;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Rapport;
import com.orleansgo.administrateur.model.TypeRapport;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.RapportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RapportService {
    
    private final RapportRepository rapportRepository;
    private final AdministrateurRepository administrateurRepository;
    
    public List<RapportDTO> getAllRapports() {
        return rapportRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RapportDTO> getRapportsByType(TypeRapport type) {
        return rapportRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RapportDTO> getRapportsByDateRange(LocalDateTime debut, LocalDateTime fin) {
        return rapportRepository.findByDateGenerationBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RapportDTO getRapport(String id) {
        return rapportRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Rapport non trouvé avec l'ID: " + id));
    }
    
    @Transactional
    public RapportDTO genererRapport(TypeRapport type, String administrateurId) {
        Administrateur administrateur = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + administrateurId));
        
        // Logique de génération de rapport selon le type
        // Ce serait normalement un processus plus complexe avec des appels à d'autres services
        String contenu = genererContenuRapport(type);
        String nomFichier = "rapport_" + type.name().toLowerCase() + "_" + LocalDateTime.now().toString().replace(":", "-") + ".pdf";
        
        Rapport rapport = new Rapport();
        rapport.setType(type);
        rapport.setContenu(contenu);
        rapport.setNomFichier(nomFichier);
        rapport.setFormatFichier("PDF");
        rapport.setDateGeneration(LocalDateTime.now());
        rapport.setAdministrateur(administrateur);
        
        return convertToDTO(rapportRepository.save(rapport));
    }
    
    private String genererContenuRapport(TypeRapport type) {
        // Simulation de génération de contenu selon le type
        switch (type) {
            case FINANCIER_QUOTIDIEN:
                return "Rapport financier quotidien généré le " + LocalDateTime.now();
            case FINANCIER_MENSUEL:
                return "Rapport financier mensuel généré le " + LocalDateTime.now();
            case ACTIVITE_CONDUCTEURS:
                return "Rapport d'activité des conducteurs généré le " + LocalDateTime.now();
            case ACTIVITE_COURSES:
                return "Rapport d'activité des courses généré le " + LocalDateTime.now();
            case SATISFACTION_USAGERS:
                return "Rapport de satisfaction des usagers généré le " + LocalDateTime.now();
            case INCIDENTS:
                return "Rapport des incidents généré le " + LocalDateTime.now();
            default:
                return "Rapport généré le " + LocalDateTime.now();
        }
    }
    
    @Transactional
    public void deleteRapport(String id) {
        if (!rapportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rapport non trouvé avec l'ID: " + id);
        }
        rapportRepository.deleteById(id);
    }
    
    private RapportDTO convertToDTO(Rapport rapport) {
        RapportDTO dto = new RapportDTO();
        dto.setId(rapport.getId());
        dto.setType(rapport.getType());
        dto.setContenu(rapport.getContenu());
        dto.setNomFichier(rapport.getNomFichier());
        dto.setFormatFichier(rapport.getFormatFichier());
        dto.setDateGeneration(rapport.getDateGeneration());
        dto.setAdministrateurId(rapport.getAdministrateur() != null ? rapport.getAdministrateur().getId() : null);
        return dto;
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.RapportDTO;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Rapport;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.RapportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RapportService {

    private final RapportRepository rapportRepository;
    private final AdministrateurRepository administrateurRepository;

    @Autowired
    public RapportService(RapportRepository rapportRepository, AdministrateurRepository administrateurRepository) {
        this.rapportRepository = rapportRepository;
        this.administrateurRepository = administrateurRepository;
    }

    @Transactional(readOnly = true)
    public List<RapportDTO> getAllRapports() {
        return rapportRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RapportDTO> getRapportById(UUID id) {
        return rapportRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public RapportDTO createRapport(RapportDTO rapportDTO) {
        Rapport rapport = convertToEntity(rapportDTO);
        rapport.setDateCreation(LocalDateTime.now());
        return convertToDTO(rapportRepository.save(rapport));
    }

    @Transactional
    public Optional<RapportDTO> updateRapport(UUID id, RapportDTO rapportDTO) {
        if (rapportRepository.existsById(id)) {
            Rapport rapport = convertToEntity(rapportDTO);
            rapport.setId(id);
            return Optional.of(convertToDTO(rapportRepository.save(rapport)));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteRapport(UUID id) {
        if (rapportRepository.existsById(id)) {
            rapportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<RapportDTO> getRapportsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return rapportRepository.findByPeriodeDuBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RapportDTO> getRapportsByType(String typeDonnees) {
        return rapportRepository.findByTypeDonnees(typeDonnees).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RapportDTO> getRapportsByAdministrateur(UUID adminId) {
        return rapportRepository.findByCreePar_Id(adminId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RapportDTO convertToDTO(Rapport rapport) {
        RapportDTO dto = new RapportDTO();
        dto.setId(rapport.getId());
        dto.setTitre(rapport.getTitre());
        dto.setDescription(rapport.getDescription());
        dto.setTypeDonnees(rapport.getTypeDonnees());
        dto.setPeriodeDu(rapport.getPeriodeDu());
        dto.setPeriodeAu(rapport.getPeriodeAu());
        dto.setContenuJson(rapport.getContenuJson());
        dto.setActif(rapport.isActif());
        dto.setDateCreation(rapport.getDateCreation());
        
        if (rapport.getCreePar() != null) {
            dto.setCreeParId(rapport.getCreePar().getId());
        }
        
        return dto;
    }

    private Rapport convertToEntity(RapportDTO dto) {
        Rapport rapport = new Rapport();
        rapport.setId(dto.getId());
        rapport.setTitre(dto.getTitre());
        rapport.setDescription(dto.getDescription());
        rapport.setTypeDonnees(dto.getTypeDonnees());
        rapport.setPeriodeDu(dto.getPeriodeDu());
        rapport.setPeriodeAu(dto.getPeriodeAu());
        rapport.setContenuJson(dto.getContenuJson());
        rapport.setActif(dto.isActif());
        rapport.setDateCreation(dto.getDateCreation());
        
        if (dto.getCreeParId() != null) {
            administrateurRepository.findById(dto.getCreeParId())
                .ifPresent(rapport::setCreePar);
        }
        
        return rapport;
    }
}
