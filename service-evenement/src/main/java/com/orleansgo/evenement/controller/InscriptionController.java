
package com.orleansgo.evenement.controller;

import com.orleansgo.evenement.dto.InscriptionDTO;
import com.orleansgo.evenement.model.StatutInscription;
import com.orleansgo.evenement.service.InscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public ResponseEntity<List<InscriptionDTO>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscriptionDTO> getInscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.getInscriptionById(id));
    }

    @PostMapping
    public ResponseEntity<InscriptionDTO> createInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) {
        return new ResponseEntity<>(inscriptionService.createInscription(inscriptionDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<InscriptionDTO> updateStatutInscription(
            @PathVariable Long id,
            @RequestParam StatutInscription statut) {
        return ResponseEntity.ok(inscriptionService.updateStatutInscription(id, statut));
    }

    @PatchMapping("/{id}/confirmer-paiement")
    public ResponseEntity<InscriptionDTO> confirmerPaiement(
            @PathVariable Long id,
            @RequestParam String referenceTransaction) {
        return ResponseEntity.ok(inscriptionService.confirmerPaiement(id, referenceTransaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/evenement/{evenementId}")
    public ResponseEntity<List<InscriptionDTO>> getInscriptionsByEvenement(@PathVariable Long evenementId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByEvenement(evenementId));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<InscriptionDTO>> getInscriptionsByUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByUtilisateur(utilisateurId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<InscriptionDTO>> getInscriptionsByStatut(@PathVariable StatutInscription statut) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByStatut(statut));
    }

    @GetMapping("/utilisateur/{utilisateurId}/statut/{statut}")
    public ResponseEntity<List<InscriptionDTO>> getInscriptionsByUtilisateurAndStatut(
            @PathVariable Long utilisateurId,
            @PathVariable StatutInscription statut) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByUtilisateurAndStatut(utilisateurId, statut));
    }

    @GetMapping("/evenement/{evenementId}/statut/{statut}")
    public ResponseEntity<List<InscriptionDTO>> getInscriptionsByEvenementAndStatut(
            @PathVariable Long evenementId,
            @PathVariable StatutInscription statut) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByEvenementAndStatut(evenementId, statut));
    }
}
