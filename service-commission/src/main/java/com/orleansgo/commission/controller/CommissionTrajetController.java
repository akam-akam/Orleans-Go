
package com.orleansgo.commission.controller;

import com.orleansgo.commission.dto.CommissionTrajetDTO;
import com.orleansgo.commission.service.CommissionTrajetService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commissions-trajets")
@RequiredArgsConstructor
public class CommissionTrajetController {

    private final CommissionTrajetService commissionTrajetService;

    @GetMapping
    public ResponseEntity<List<CommissionTrajetDTO>> getAllCommissionsTrajet() {
        return ResponseEntity.ok(commissionTrajetService.getAllCommissionsTrajet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionTrajetDTO> getCommissionTrajetById(@PathVariable Long id) {
        return ResponseEntity.ok(commissionTrajetService.getCommissionTrajetById(id));
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<CommissionTrajetDTO>> getCommissionsTrajetByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(commissionTrajetService.getCommissionsTrajetByChauffeur(chauffeurId));
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<CommissionTrajetDTO>> getCommissionsTrajetByTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(commissionTrajetService.getCommissionsTrajetByTrajet(trajetId));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<CommissionTrajetDTO>> getCommissionsTrajetByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(commissionTrajetService.getCommissionsTrajetByPeriode(debut, fin));
    }

    @GetMapping("/total-periode")
    public ResponseEntity<BigDecimal> getTotalCommissionsForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(commissionTrajetService.calculateTotalCommissionsForPeriod(debut, fin));
    }

    @GetMapping("/total-chauffeur/{chauffeurId}")
    public ResponseEntity<BigDecimal> getTotalCommissionsForChauffeur(
            @PathVariable Long chauffeurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(commissionTrajetService.calculateTotalCommissionsForChauffeur(chauffeurId, debut, fin));
    }

    @PostMapping
    public ResponseEntity<CommissionTrajetDTO> createCommissionTrajet(@Valid @RequestBody CommissionTrajetDTO commissionTrajetDTO) {
        return new ResponseEntity<>(commissionTrajetService.createCommissionTrajet(commissionTrajetDTO), HttpStatus.CREATED);
    }
}
