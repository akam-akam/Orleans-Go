
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.service.StatistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistiquesCourses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesCourses(debut, fin));
    }

    @GetMapping("/chauffeurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistiquesChauffeurs() {
        return ResponseEntity.ok(statistiqueService.getStatistiquesChauffeurs());
    }

    @GetMapping("/utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistiquesUtilisateurs() {
        return ResponseEntity.ok(statistiqueService.getStatistiquesUtilisateurs());
    }

    @PostMapping("/generer-rapport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> genererRapportStatistiques() {
        statistiqueService.genererRapportStatistiques();
        return ResponseEntity.ok().build();
    }
}
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.StatistiqueDTO;
import com.orleansgo.administrateur.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping
    public ResponseEntity<List<StatistiqueDTO>> getAllStatistiques() {
        return ResponseEntity.ok(statistiqueService.getAllStatistiques());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatistiqueDTO> getStatistiqueById(@PathVariable Long id) {
        return ResponseEntity.ok(statistiqueService.getStatistiqueById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<StatistiqueDTO>> getStatistiquesByType(@PathVariable String type) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesByType(type));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<StatistiqueDTO>> getStatistiquesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesByPeriode(debut, fin));
    }

    @PostMapping
    public ResponseEntity<StatistiqueDTO> createStatistique(@RequestBody StatistiqueDTO statistiqueDTO) {
        return new ResponseEntity<>(statistiqueService.createStatistique(statistiqueDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatistiqueDTO> updateStatistique(@PathVariable Long id, @RequestBody StatistiqueDTO statistiqueDTO) {
        return ResponseEntity.ok(statistiqueService.updateStatistique(id, statistiqueDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatistique(@PathVariable Long id) {
        statistiqueService.deleteStatistique(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getStatistiquesCourses() {
        // Récupérer les statistiques actuelles des courses
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debutJour = maintenant.toLocalDate().atStartOfDay();
        LocalDateTime debutSemaine = maintenant.toLocalDate().minusDays(maintenant.getDayOfWeek().getValue() - 1).atStartOfDay();
        LocalDateTime debutMois = maintenant.toLocalDate().withDayOfMonth(1).atStartOfDay();

        Map<String, Object> resultat = new HashMap<>();
        
        // Nombre de courses aujourd'hui
        Double coursesAujourdhui = statistiqueService.getSumByTypeAndPeriode("COURSE_COMPTEUR", debutJour, maintenant);
        resultat.put("coursesAujourdhui", coursesAujourdhui != null ? coursesAujourdhui.intValue() : 0);
        
        // Nombre de courses cette semaine
        Double coursesSemaine = statistiqueService.getSumByTypeAndPeriode("COURSE_COMPTEUR", debutSemaine, maintenant);
        resultat.put("coursesSemaine", coursesSemaine != null ? coursesSemaine.intValue() : 0);
        
        // Nombre de courses ce mois
        Double coursesMois = statistiqueService.getSumByTypeAndPeriode("COURSE_COMPTEUR", debutMois, maintenant);
        resultat.put("coursesMois", coursesMois != null ? coursesMois.intValue() : 0);
        
        // Revenu des courses aujourd'hui
        Double revenuAujourdhui = statistiqueService.getSumByTypeAndPeriode("COURSE_REVENU", debutJour, maintenant);
        resultat.put("revenuAujourdhui", revenuAujourdhui != null ? revenuAujourdhui : 0.0);
        
        // Revenu des courses cette semaine
        Double revenuSemaine = statistiqueService.getSumByTypeAndPeriode("COURSE_REVENU", debutSemaine, maintenant);
        resultat.put("revenuSemaine", revenuSemaine != null ? revenuSemaine : 0.0);
        
        // Revenu des courses ce mois
        Double revenuMois = statistiqueService.getSumByTypeAndPeriode("COURSE_REVENU", debutMois, maintenant);
        resultat.put("revenuMois", revenuMois != null ? revenuMois : 0.0);
        
        return ResponseEntity.ok(resultat);
    }

    @GetMapping("/chauffeurs")
    public ResponseEntity<Map<String, Object>> getStatistiquesChauffeurs() {
        // Implémenter la logique similaire à celle des courses
        Map<String, Object> resultat = new HashMap<>();
        // Ajouter les statistiques des chauffeurs...
        return ResponseEntity.ok(resultat);
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<Map<String, Object>> getStatistiquesUtilisateurs() {
        // Implémenter la logique similaire à celle des courses
        Map<String, Object> resultat = new HashMap<>();
        // Ajouter les statistiques des utilisateurs...
        return ResponseEntity.ok(resultat);
    }

    @PostMapping("/generer-rapport")
    public ResponseEntity<Map<String, Object>> genererRapportStatistique() {
        // Générer un rapport statistique complet
        Map<String, Object> rapport = new HashMap<>();
        rapport.put("courses", statistiqueService.getStatistiquesByType("COURSE"));
        rapport.put("chauffeurs", statistiqueService.getStatistiquesByType("CHAUFFEUR"));
        rapport.put("utilisateurs", statistiqueService.getStatistiquesByType("UTILISATEUR"));
        rapport.put("paiements", statistiqueService.getStatistiquesByType("PAIEMENT"));
        rapport.put("dateGeneration", LocalDateTime.now());
        
        return ResponseEntity.ok(rapport);
    }
}
