
package com.orleansgo.commission.controller;

import com.orleansgo.commission.dto.CommissionDTO;
import com.orleansgo.commission.service.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/actives")
    public ResponseEntity<List<CommissionDTO>> getActiveCommissions() {
        return ResponseEntity.ok(commissionService.getActiveCommissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionDTO> getCommissionById(@PathVariable Long id) {
        return ResponseEntity.ok(commissionService.getCommissionById(id));
    }

    @PostMapping
    public ResponseEntity<CommissionDTO> createCommission(@Valid @RequestBody CommissionDTO commissionDTO) {
        return new ResponseEntity<>(commissionService.createCommission(commissionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommissionDTO> updateCommission(@PathVariable Long id, @Valid @RequestBody CommissionDTO commissionDTO) {
        return ResponseEntity.ok(commissionService.updateCommission(id, commissionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        commissionService.deleteCommission(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CommissionDTO> activateCommission(@PathVariable Long id) {
        return ResponseEntity.ok(commissionService.activateCommission(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CommissionDTO> deactivateCommission(@PathVariable Long id) {
        return ResponseEntity.ok(commissionService.deactivateCommission(id));
    }
}
