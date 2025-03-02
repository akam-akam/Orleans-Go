
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
