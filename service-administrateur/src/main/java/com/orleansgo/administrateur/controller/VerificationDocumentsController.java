
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.VerificationDocumentsDTO;
import com.orleansgo.administrateur.model.StatutVerification;
import com.orleansgo.administrateur.service.VerificationDocumentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
public class VerificationDocumentsController {
    
    private final VerificationDocumentsService verificationService;
    
    @GetMapping
    public ResponseEntity<List<VerificationDocumentsDTO>> getAllVerifications() {
        return ResponseEntity.ok(verificationService.getAllVerifications());
    }
    
    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<VerificationDocumentsDTO>> getVerificationsByChauffeur(@PathVariable String chauffeurId) {
        return ResponseEntity.ok(verificationService.getVerificationsByChauffeur(chauffeurId));
    }
    
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<VerificationDocumentsDTO>> getVerificationsByStatut(@PathVariable StatutVerification statut) {
        return ResponseEntity.ok(verificationService.getVerificationsByStatut(statut));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VerificationDocumentsDTO> getVerification(@PathVariable String id) {
        return ResponseEntity.ok(verificationService.getVerification(id));
    }
    
    @PostMapping
    public ResponseEntity<VerificationDocumentsDTO> createVerification(@Valid @RequestBody VerificationDocumentsDTO verificationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(verificationService.createVerification(verificationDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VerificationDocumentsDTO> updateVerification(
            @PathVariable String id,
            @Valid @RequestBody VerificationDocumentsDTO verificationDTO) {
        return ResponseEntity.ok(verificationService.updateVerification(id, verificationDTO));
    }
    
    @PatchMapping("/{id}/statut")
    public ResponseEntity<VerificationDocumentsDTO> updateStatut(
            @PathVariable String id,
            @RequestParam StatutVerification statut,
            @RequestParam String administrateurId,
            @RequestParam(required = false) String commentaires) {
        return ResponseEntity.ok(verificationService.updateStatut(id, statut, administrateurId, commentaires));
    }
}
