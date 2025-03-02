
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.CommissionDTO;
import com.orleansgo.administrateur.service.CommissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
@RequiredArgsConstructor
public class CommissionController {
    
    private final CommissionService commissionService;
    
    @GetMapping
    public ResponseEntity<List<CommissionDTO>> getAllCommissions() {
        return ResponseEntity.ok(commissionService.getAllCommissions());
    }
    
    @GetMapping("/active")
    public ResponseEntity<CommissionDTO> getActiveCommission() {
        return ResponseEntity.ok(commissionService.getActiveCommission());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CommissionDTO> getCommission(@PathVariable String id) {
        return ResponseEntity.ok(commissionService.getCommission(id));
    }
    
    @PostMapping
    public ResponseEntity<CommissionDTO> createCommission(@Valid @RequestBody CommissionDTO commissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commissionService.createCommission(commissionDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CommissionDTO> updateCommission(
            @PathVariable String id,
            @Valid @RequestBody CommissionDTO commissionDTO) {
        return ResponseEntity.ok(commissionService.updateCommission(id, commissionDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable String id) {
        commissionService.deleteCommission(id);
        return ResponseEntity.noContent().build();
    }
}
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Commission;
import com.orleansgo.administrateur.service.AdministrateurService;
import com.orleansgo.administrateur.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/commissions")
@RequiredArgsConstructor
@Tag(name = "Commission", description = "API de gestion des commissions")
public class CommissionController {
    private final CommissionService commissionService;
    private final AdministrateurService administrateurService;
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les commissions")
    public ResponseEntity<List<Commission>> getAllCommissions() {
        return ResponseEntity.ok(commissionService.findAllCommissions());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une commission par son ID")
    public ResponseEntity<Commission> getCommissionById(@PathVariable Long id) {
        return commissionService.findCommissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/current")
    @Operation(summary = "Récupérer la commission actuelle")
    public ResponseEntity<Commission> getCurrentCommission() {
        return commissionService.findCurrentCommission()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle commission")
    public ResponseEntity<Commission> createCommission(@RequestBody Commission commission, @RequestParam Long administrateurId) {
        return administrateurService.findAdministrateurById(administrateurId)
                .map(admin -> ResponseEntity.ok(commissionService.createCommission(commission, admin)))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une commission")
    public ResponseEntity<Commission> updateCommission(@PathVariable Long id, @RequestBody Commission commission, @RequestParam Long administrateurId) {
        Administrateur admin = administrateurService.findAdministrateurById(administrateurId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return commissionService.updateCommission(id, commission, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/cloturer")
    @Operation(summary = "Clôturer une commission")
    public ResponseEntity<Commission> clotureCommission(@PathVariable Long id, @RequestParam Long administrateurId) {
        Administrateur admin = administrateurService.findAdministrateurById(administrateurId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return commissionService.clotureCommission(id, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/ajuster")
    @Operation(summary = "Ajuster le taux de commission")
    public ResponseEntity<Commission> ajusterCommission(@RequestParam BigDecimal nouveauTaux, @RequestParam Long administrateurId) {
        return administrateurService.findAdministrateurById(administrateurId)
                .map(admin -> ResponseEntity.ok(commissionService.ajusterCommission(nouveauTaux, admin)))
                .orElse(ResponseEntity.badRequest().build());
    }
}
