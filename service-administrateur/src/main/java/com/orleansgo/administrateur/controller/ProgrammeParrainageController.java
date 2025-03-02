
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.ProgrammeParrainage;
import com.orleansgo.administrateur.service.AdministrateurService;
import com.orleansgo.administrateur.service.ProgrammeParrainageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/parrainages")
@RequiredArgsConstructor
@Tag(name = "Parrainage", description = "API de gestion des programmes de parrainage")
public class ProgrammeParrainageController {
    private final ProgrammeParrainageService programmeParrainageService;
    private final AdministrateurService administrateurService;
    
    @GetMapping
    @Operation(summary = "Récupérer tous les programmes de parrainage")
    public ResponseEntity<List<ProgrammeParrainage>> getAllProgrammes() {
        return ResponseEntity.ok(programmeParrainageService.findAllProgrammes());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un programme de parrainage par son ID")
    public ResponseEntity<ProgrammeParrainage> getProgrammeById(@PathVariable Long id) {
        return programmeParrainageService.findProgrammeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/current")
    @Operation(summary = "Récupérer le programme de parrainage actuel")
    public ResponseEntity<ProgrammeParrainage> getCurrentProgramme() {
        return programmeParrainageService.findCurrentProgramme()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau programme de parrainage")
    public ResponseEntity<ProgrammeParrainage> createProgramme(@RequestBody ProgrammeParrainage programme, @RequestParam Long administrateurId) {
        return administrateurService.findAdministrateurById(administrateurId)
                .map(admin -> ResponseEntity.ok(programmeParrainageService.createProgramme(programme, admin)))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un programme de parrainage")
    public ResponseEntity<ProgrammeParrainage> updateProgramme(@PathVariable Long id, @RequestBody ProgrammeParrainage programme, @RequestParam Long administrateurId) {
        Administrateur admin = administrateurService.findAdministrateurById(administrateurId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return programmeParrainageService.updateProgramme(id, programme, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/cloturer")
    @Operation(summary = "Clôturer un programme de parrainage")
    public ResponseEntity<ProgrammeParrainage> clotureProgramme(@PathVariable Long id, @RequestParam Long administrateurId) {
        Administrateur admin = administrateurService.findAdministrateurById(administrateurId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return programmeParrainageService.clotureProgramme(id, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/configurer")
    @Operation(summary = "Configurer un nouveau programme de parrainage")
    public ResponseEntity<ProgrammeParrainage> configurerParrainage(
            @RequestParam BigDecimal bonusParrain,
            @RequestParam BigDecimal bonusFilleul,
            @RequestParam Long administrateurId) {
        
        return administrateurService.findAdministrateurById(administrateurId)
                .map(admin -> ResponseEntity.ok(programmeParrainageService.configurerParrainage(bonusParrain, bonusFilleul, admin)))
                .orElse(ResponseEntity.badRequest().build());
    }
}
