
package com.orleansgo.vehicule.controller;

import com.orleansgo.vehicule.dto.VehiculeDTO;
import com.orleansgo.vehicule.model.StatutVehicule;
import com.orleansgo.vehicule.model.TypeVehicule;
import com.orleansgo.vehicule.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {
    
    @Autowired
    private VehiculeService vehiculeService;
    
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules() {
        return ResponseEntity.ok(vehiculeService.getAllVehicules());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehiculeById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.getVehiculeById(id));
    }
    
    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByChauffeur(chauffeurId));
    }
    
    @GetMapping("/type/{typeVehicule}")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByType(@PathVariable TypeVehicule typeVehicule) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByType(typeVehicule));
    }
    
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByStatut(@PathVariable StatutVehicule statut) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByStatut(statut));
    }
    
    @GetMapping("/filtrer")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByTypeAndStatut(
            @RequestParam TypeVehicule type, 
            @RequestParam StatutVehicule statut) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByTypeAndStatut(type, statut));
    }
    
    @PostMapping
    public ResponseEntity<VehiculeDTO> createVehicule(@RequestBody VehiculeDTO vehiculeDTO) {
        return new ResponseEntity<>(vehiculeService.createVehicule(vehiculeDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> updateVehicule(@PathVariable Long id, @RequestBody VehiculeDTO vehiculeDTO) {
        return ResponseEntity.ok(vehiculeService.updateVehicule(id, vehiculeDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        vehiculeService.deleteVehicule(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/statut")
    public ResponseEntity<VehiculeDTO> changerStatutVehicule(
            @PathVariable Long id, 
            @RequestParam StatutVehicule nouveauStatut) {
        return ResponseEntity.ok(vehiculeService.changerStatutVehicule(id, nouveauStatut));
    }
}
