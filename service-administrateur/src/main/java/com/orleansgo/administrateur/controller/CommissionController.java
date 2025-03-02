
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
