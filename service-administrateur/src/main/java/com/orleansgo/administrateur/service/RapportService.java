
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
