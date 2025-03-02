
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.RapportDTO;
import com.orleansgo.administrateur.model.TypeRapport;
import com.orleansgo.administrateur.service.RapportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rapports")
@RequiredArgsConstructor
public class RapportController {
    
    private final RapportService rapportService;
    
    @GetMapping
    public ResponseEntity<List<RapportDTO>> getAllRapports() {
        return ResponseEntity.ok(rapportService.getAllRapports());
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RapportDTO>> getRapportsByType(@PathVariable TypeRapport type) {
        return ResponseEntity.ok(rapportService.getRapportsByType(type));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<RapportDTO>> getRapportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(rapportService.getRapportsByDateRange(debut, fin));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RapportDTO> getRapport(@PathVariable String id) {
        return ResponseEntity.ok(rapportService.getRapport(id));
    }
    
    @PostMapping("/generer")
    public ResponseEntity<RapportDTO> genererRapport(
            @RequestParam TypeRapport type,
            @RequestParam String administrateurId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rapportService.genererRapport(type, administrateurId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapport(@PathVariable String id) {
        rapportService.deleteRapport(id);
        return ResponseEntity.noContent().build();
    }
}
