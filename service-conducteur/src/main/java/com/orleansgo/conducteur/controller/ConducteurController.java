
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
package com.orleansgo.conducteur.controller;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.dto.DocumentDTO;
import com.orleansgo.conducteur.dto.VehiculeConducteurDTO;
import com.orleansgo.conducteur.model.StatutConducteur;
import com.orleansgo.conducteur.model.StatutVehiculeConducteur;
import com.orleansgo.conducteur.service.ConducteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ConducteurDTO> getConducteurById(@PathVariable Long id) {
        return ResponseEntity.ok(conducteurService.getConducteurById(id));
    }
    
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<ConducteurDTO> getConducteurByUtilisateurId(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(conducteurService.getConducteurByUtilisateurId(utilisateurId));
    }
    
    @PostMapping
    public ResponseEntity<ConducteurDTO> createConducteur(@RequestBody ConducteurDTO conducteurDTO) {
        return new ResponseEntity<>(conducteurService.createConducteur(conducteurDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ConducteurDTO> updateConducteur(@PathVariable Long id, @RequestBody ConducteurDTO conducteurDTO) {
        return ResponseEntity.ok(conducteurService.updateConducteur(id, conducteurDTO));
    }
    
    @PatchMapping("/{id}/statut")
    public ResponseEntity<ConducteurDTO> updateStatut(@PathVariable Long id, @RequestBody Map<String, String> statut) {
        return ResponseEntity.ok(conducteurService.updateStatut(id, StatutConducteur.valueOf(statut.get("statut"))));
    }
    
    @PatchMapping("/{id}/position")
    public ResponseEntity<ConducteurDTO> updatePosition(@PathVariable Long id, @RequestBody Map<String, Double> position) {
        return ResponseEntity.ok(conducteurService.updatePosition(id, position.get("latitude"), position.get("longitude")));
    }
    
    @PatchMapping("/{id}/disponibilite")
    public ResponseEntity<ConducteurDTO> updateDisponibilite(@PathVariable Long id, @RequestBody Map<String, Boolean> disponibilite) {
        return ResponseEntity.ok(conducteurService.updateDisponibilite(id, disponibilite.get("disponible")));
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<ConducteurDTO>> getAvailableDrivers() {
        return ResponseEntity.ok(conducteurService.getAvailableDrivers());
    }
    
    @GetMapping("/proximite")
    public ResponseEntity<List<ConducteurDTO>> getNearbyAvailableDrivers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double distanceKm) {
        return ResponseEntity.ok(conducteurService.getNearbyAvailableDrivers(latitude, longitude, distanceKm));
    }
    
    @GetMapping("/{id}/documents")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByConducteurId(@PathVariable Long id) {
        return ResponseEntity.ok(conducteurService.getDocumentsByConducteurId(id));
    }
    
    @PostMapping("/{id}/documents")
    public ResponseEntity<DocumentDTO> ajouterDocument(@PathVariable Long id, @RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(conducteurService.ajouterDocument(id, documentDTO), HttpStatus.CREATED);
    }
    
    @PatchMapping("/documents/{id}/validation")
    public ResponseEntity<DocumentDTO> validerDocument(
            @PathVariable Long id,
            @RequestParam Boolean valide,
            @RequestParam(required = false) String commentaire,
            @RequestParam Long administrateurId) {
        return ResponseEntity.ok(conducteurService.validerDocument(id, valide, commentaire, administrateurId));
    }
    
    @GetMapping("/{id}/vehicules")
    public ResponseEntity<List<VehiculeConducteurDTO>> getVehiculesByConducteurId(@PathVariable Long id) {
        return ResponseEntity.ok(conducteurService.getVehiculesByConducteurId(id));
    }
    
    @PostMapping("/{id}/vehicules")
    public ResponseEntity<VehiculeConducteurDTO> ajouterVehicule(@PathVariable Long id, @RequestBody VehiculeConducteurDTO vehiculeDTO) {
        return new ResponseEntity<>(conducteurService.ajouterVehicule(id, vehiculeDTO), HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/vehicules/{vehiculeId}/principal")
    public ResponseEntity<VehiculeConducteurDTO> definirVehiculePrincipal(@PathVariable Long id, @PathVariable Long vehiculeId) {
        return ResponseEntity.ok(conducteurService.definirVehiculePrincipal(id, vehiculeId));
    }
    
    @PatchMapping("/vehicules/{id}/statut")
    public ResponseEntity<VehiculeConducteurDTO> changerStatutVehicule(
            @PathVariable Long id,
            @RequestBody Map<String, String> statut) {
        return ResponseEntity.ok(conducteurService.changerStatutVehicule(id, StatutVehiculeConducteur.valueOf(statut.get("statut"))));
    }
}
