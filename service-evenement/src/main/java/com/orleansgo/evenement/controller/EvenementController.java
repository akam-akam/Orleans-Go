
package com.orleansgo.evenement.controller;

import com.orleansgo.evenement.dto.EvenementDTO;
import com.orleansgo.evenement.model.StatutEvenement;
import com.orleansgo.evenement.service.EvenementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;

    @GetMapping
    public ResponseEntity<List<EvenementDTO>> getAllEvenements() {
        return ResponseEntity.ok(evenementService.getAllEvenements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementDTO> getEvenementById(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.getEvenementById(id));
    }

    @PostMapping
    public ResponseEntity<EvenementDTO> createEvenement(@Valid @RequestBody EvenementDTO evenementDTO) {
        return new ResponseEntity<>(evenementService.createEvenement(evenementDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementDTO> updateEvenement(@PathVariable Long id, @Valid @RequestBody EvenementDTO evenementDTO) {
        return ResponseEntity.ok(evenementService.updateEvenement(id, evenementDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.deleteEvenement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<EvenementDTO>> getEvenementsByStatut(@PathVariable StatutEvenement statut) {
        return ResponseEntity.ok(evenementService.getEvenementsByStatut(statut));
    }

    @GetMapping("/publics")
    public ResponseEntity<List<EvenementDTO>> getEvenementsPublics() {
        return ResponseEntity.ok(evenementService.getEvenementsPublics());
    }

    @GetMapping("/periode")
    public ResponseEntity<List<EvenementDTO>> getEvenementsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(evenementService.getEvenementsByPeriode(debut, fin));
    }

    @GetMapping("/createur/{createurId}")
    public ResponseEntity<List<EvenementDTO>> getEvenementsByCreateur(@PathVariable Long createurId) {
        return ResponseEntity.ok(evenementService.getEvenementsByCreateur(createurId));
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<EvenementDTO>> getEvenementsByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(evenementService.getEvenementsByCategorie(categorie));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EvenementDTO>> searchEvenements(@RequestParam String titre, Pageable pageable) {
        return ResponseEntity.ok(evenementService.searchEvenements(titre, pageable));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<EvenementDTO> changerStatutEvenement(
            @PathVariable Long id,
            @RequestParam StatutEvenement statut) {
        return ResponseEntity.ok(evenementService.changerStatutEvenement(id, statut));
    }
}
