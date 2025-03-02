
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
