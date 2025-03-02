
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
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.RapportDTO;
import com.orleansgo.administrateur.service.RapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rapports")
public class RapportController {

    private final RapportService rapportService;

    @Autowired
    public RapportController(RapportService rapportService) {
        this.rapportService = rapportService;
    }

    @GetMapping
    public ResponseEntity<List<RapportDTO>> getAllRapports() {
        return ResponseEntity.ok(rapportService.getAllRapports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RapportDTO> getRapportById(@PathVariable UUID id) {
        return rapportService.getRapportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RapportDTO> createRapport(@RequestBody RapportDTO rapportDTO) {
        return new ResponseEntity<>(rapportService.createRapport(rapportDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RapportDTO> updateRapport(@PathVariable UUID id, @RequestBody RapportDTO rapportDTO) {
        return rapportService.updateRapport(id, rapportDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapport(@PathVariable UUID id) {
        if (rapportService.deleteRapport(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/periode")
    public ResponseEntity<List<RapportDTO>> getRapportsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(rapportService.getRapportsByPeriode(debut, fin));
    }

    @GetMapping("/type/{typeDonnees}")
    public ResponseEntity<List<RapportDTO>> getRapportsByType(@PathVariable String typeDonnees) {
        return ResponseEntity.ok(rapportService.getRapportsByType(typeDonnees));
    }

    @GetMapping("/administrateur/{adminId}")
    public ResponseEntity<List<RapportDTO>> getRapportsByAdministrateur(@PathVariable UUID adminId) {
        return ResponseEntity.ok(rapportService.getRapportsByAdministrateur(adminId));
    }
}
