
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.model.ActionUtilisateur;
import com.orleansgo.administrateur.model.RoleAdministrateur;
import com.orleansgo.administrateur.service.AdministrateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrateurs")
@RequiredArgsConstructor
public class AdministrateurController {
    
    private final AdministrateurService administrateurService;
    
    @GetMapping
    public ResponseEntity<List<AdministrateurDTO>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.getAllAdministrateurs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> getAdministrateur(@PathVariable String id) {
        return ResponseEntity.ok(administrateurService.getAdministrateur(id));
    }
    
    @PostMapping
    public ResponseEntity<AdministrateurDTO> createAdministrateur(
            @Valid @RequestBody AdministrateurDTO administrateurDTO,
            @RequestParam String motDePasse) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(administrateurService.createAdministrateur(administrateurDTO, motDePasse));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> updateAdministrateur(
            @PathVariable String id,
            @Valid @RequestBody AdministrateurDTO administrateurDTO) {
        return ResponseEntity.ok(administrateurService.updateAdministrateur(id, administrateurDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable String id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/role")
    public ResponseEntity<AdministrateurDTO> changeRole(
            @PathVariable String id,
            @RequestParam RoleAdministrateur role) {
        return ResponseEntity.ok(administrateurService.changeAdministrateurRole(id, role));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdministrateurDTO> changeStatus(
            @PathVariable String id,
            @RequestParam boolean actif) {
        return ResponseEntity.ok(administrateurService.updateAdministrateurStatus(id, actif));
    }
    
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable String id,
            @RequestParam String newPassword) {
        administrateurService.resetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/gerer-utilisateur")
    public ResponseEntity<Void> gererUtilisateur(
            @RequestParam String utilisateurId,
            @RequestParam ActionUtilisateur action,
            @RequestParam String administrateurId) {
        administrateurService.gererUtilisateur(utilisateurId, action, administrateurId);
        return ResponseEntity.ok().build();
    }
}
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.service.AdministrateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrateurs")
@RequiredArgsConstructor
@Tag(name = "Administrateur", description = "API de gestion des administrateurs")
public class AdministrateurController {
    private final AdministrateurService administrateurService;
    
    @GetMapping
    @Operation(summary = "Récupérer tous les administrateurs")
    public ResponseEntity<List<Administrateur>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.findAllAdministrateurs());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un administrateur par son ID")
    public ResponseEntity<Administrateur> getAdministrateurById(@PathVariable Long id) {
        return administrateurService.findAdministrateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouvel administrateur")
    public ResponseEntity<Administrateur> createAdministrateur(@RequestBody Administrateur administrateur) {
        return ResponseEntity.ok(administrateurService.createAdministrateur(administrateur));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un administrateur")
    public ResponseEntity<Administrateur> updateAdministrateur(@PathVariable Long id, @RequestBody Administrateur administrateur) {
        return administrateurService.updateAdministrateur(id, administrateur)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable Long id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }
}
