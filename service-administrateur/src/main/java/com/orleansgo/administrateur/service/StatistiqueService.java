
package com.orleansgo.administrateur.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatistiqueService {

    private final RapportService rapportService;

    /**
     * Génère des statistiques sur le nombre de courses par jour sur une période donnée
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesCourses(LocalDate debut, LocalDate fin) {
        Map<String, Object> stats = new HashMap<>();
        
        // Dans une implémentation réelle, ces données viendraient d'une base de données
        // ou d'un appel à un autre microservice via RestTemplate ou Feign Client
        stats.put("periode", Map.of("debut", debut, "fin", fin));
        stats.put("nombreTotalCourses", 1250);
        stats.put("coursesParJour", Map.of(
            "2025-03-01", 42,
            "2025-03-02", 38,
            "2025-03-03", 45
        ));
        stats.put("revenuTotal", 18750.50);
        stats.put("commissionsPercues", 2812.58);
        
        return stats;
    }

    /**
     * Génère des statistiques sur les chauffeurs (performances, évaluations)
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesChauffeurs() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("nombreTotalChauffeurs", 85);
        stats.put("chauffeursActifs", 72);
        stats.put("nouvellementInscrits", 12);
        stats.put("evaluationMoyenne", 4.7);
        stats.put("topChauffeurs", Map.of(
            "chauffeur1", Map.of("id", 1, "nom", "Dupont", "courses", 45, "evaluation", 4.9),
            "chauffeur2", Map.of("id", 2, "nom", "Martin", "courses", 42, "evaluation", 4.8),
            "chauffeur3", Map.of("id", 3, "nom", "Durand", "courses", 38, "evaluation", 4.8)
        ));
        
        return stats;
    }

    /**
     * Génère des statistiques sur les utilisateurs (inscriptions, activité)
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesUtilisateurs() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("nombreTotalUtilisateurs", 1250);
        stats.put("utilisateursActifsMois", 950);
        stats.put("nouveauxUtilisateurs", 125);
        stats.put("utilisateursParrainage", 78);
        stats.put("retentionRate", 0.85);
        
        return stats;
    }

    /**
     * Génère un rapport complet avec toutes les statistiques
     */
    @Transactional
    public void genererRapportStatistiques() {
        LocalDate debut = LocalDate.now().minusDays(30);
        LocalDate fin = LocalDate.now();
        
        Map<String, Object> donnees = new HashMap<>();
        donnees.put("statistiquesCourses", getStatistiquesCourses(debut, fin));
        donnees.put("statistiquesChauffeurs", getStatistiquesChauffeurs());
        donnees.put("statistiquesUtilisateurs", getStatistiquesUtilisateurs());
        donnees.put("dateGeneration", LocalDateTime.now());
        
        // Enregistrer le rapport généré
        rapportService.creerRapportStatistique("Rapport mensuel", donnees);
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.StatistiqueDTO;
import com.orleansgo.administrateur.model.Statistique;
import com.orleansgo.administrateur.repository.StatistiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatistiqueService {

    @Autowired
    private StatistiqueRepository statistiqueRepository;

    public List<StatistiqueDTO> getAllStatistiques() {
        return statistiqueRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StatistiqueDTO getStatistiqueById(Long id) {
        Statistique statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistique non trouvée avec l'ID: " + id));
        return convertToDTO(statistique);
    }

    public List<StatistiqueDTO> getStatistiquesByType(String type) {
        return statistiqueRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StatistiqueDTO> getStatistiquesByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return statistiqueRepository.findByPeriodeBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StatistiqueDTO createStatistique(StatistiqueDTO statistiqueDTO) {
        Statistique statistique = convertToEntity(statistiqueDTO);
        statistique = statistiqueRepository.save(statistique);
        return convertToDTO(statistique);
    }

    public StatistiqueDTO updateStatistique(Long id, StatistiqueDTO statistiqueDTO) {
        if (!statistiqueRepository.existsById(id)) {
            throw new RuntimeException("Statistique non trouvée avec l'ID: " + id);
        }
        Statistique statistique = convertToEntity(statistiqueDTO);
        statistique.setId(id);
        statistique = statistiqueRepository.save(statistique);
        return convertToDTO(statistique);
    }

    public void deleteStatistique(Long id) {
        if (!statistiqueRepository.existsById(id)) {
            throw new RuntimeException("Statistique non trouvée avec l'ID: " + id);
        }
        statistiqueRepository.deleteById(id);
    }

    public Double getSumByTypeAndPeriode(String type, LocalDateTime debut, LocalDateTime fin) {
        return statistiqueRepository.calculateSumByTypeAndPeriode(type, debut, fin);
    }

    // Méthodes utilitaires
    
    private StatistiqueDTO convertToDTO(Statistique statistique) {
        StatistiqueDTO dto = new StatistiqueDTO();
        dto.setId(statistique.getId());
        dto.setType(statistique.getType());
        dto.setLabel(statistique.getLabel());
        dto.setValeur(statistique.getValeur());
        dto.setDateCreation(statistique.getDateCreation());
        dto.setPeriode(statistique.getPeriode());
        dto.setMetadonnees(statistique.getMetadonnees());
        return dto;
    }

    private Statistique convertToEntity(StatistiqueDTO dto) {
        Statistique statistique = new Statistique();
        statistique.setId(dto.getId());
        statistique.setType(dto.getType());
        statistique.setLabel(dto.getLabel());
        statistique.setValeur(dto.getValeur());
        statistique.setDateCreation(dto.getDateCreation());
        statistique.setPeriode(dto.getPeriode());
        statistique.setMetadonnees(dto.getMetadonnees());
        return statistique;
    }
}
