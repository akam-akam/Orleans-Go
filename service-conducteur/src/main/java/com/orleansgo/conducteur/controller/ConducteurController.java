
package com.orleansgo.conducteur.controller;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.service.ConducteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conducteurs")
@RequiredArgsConstructor
public class ConducteurController {

    private final ConducteurService conducteurService;

    @GetMapping
    public ResponseEntity<List<ConducteurDTO>> getAllConducteurs() {
        return ResponseEntity.ok(conducteurService.getAllConducteurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConducteurDTO> getConducteurById(@PathVariable UUID id) {
        return ResponseEntity.ok(conducteurService.getConducteurById(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<ConducteurDTO> getConducteurByUtilisateurId(@PathVariable UUID utilisateurId) {
        return ResponseEntity.ok(conducteurService.getConducteurByUtilisateurId(utilisateurId));
    }

    @PostMapping
    public ResponseEntity<ConducteurDTO> createConducteur(@RequestParam UUID utilisateurId) {
        return new ResponseEntity<>(conducteurService.createConducteur(utilisateurId), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/disponibilite")
    public ResponseEntity<ConducteurDTO> updateDisponibilite(
            @PathVariable UUID id,
            @RequestParam boolean disponible) {
        return ResponseEntity.ok(conducteurService.updateDisponibilite(id, disponible));
    }

    @PatchMapping("/{id}/gains")
    public ResponseEntity<ConducteurDTO> ajouterGains(
            @PathVariable UUID id,
            @RequestParam @Valid BigDecimal montant) {
        return ResponseEntity.ok(conducteurService.ajouterGains(id, montant));
    }

    @PatchMapping("/{id}/documents")
    public ResponseEntity<ConducteurDTO> updateDocumentsValides(
            @PathVariable UUID id,
            @RequestParam boolean documentsValides) {
        return ResponseEntity.ok(conducteurService.updateDocumentsValides(id, documentsValides));
    }
}
