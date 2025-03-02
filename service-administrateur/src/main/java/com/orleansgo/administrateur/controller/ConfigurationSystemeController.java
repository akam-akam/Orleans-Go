
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.model.ConfigurationSysteme;
import com.orleansgo.administrateur.service.ConfigurationSystemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/configurations")
@RequiredArgsConstructor
public class ConfigurationSystemeController {

    private final ConfigurationSystemeService configurationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConfigurationSysteme>> getAllConfigurations() {
        return ResponseEntity.ok(configurationService.getAllConfigurations());
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConfigurationSysteme>> getConfigurationsByType(
            @PathVariable ConfigurationSysteme.TypeConfiguration type) {
        return ResponseEntity.ok(configurationService.getConfigurationsByType(type));
    }

    @GetMapping("/{cle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfigurationSysteme> getConfigurationByCle(@PathVariable String cle) {
        return ResponseEntity.ok(configurationService.getConfigurationByCle(cle));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfigurationSysteme> createConfiguration(
            @Valid @RequestBody ConfigurationSysteme configuration) {
        return new ResponseEntity<>(configurationService.saveConfiguration(configuration), HttpStatus.CREATED);
    }

    @PutMapping("/{cle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfigurationSysteme> updateConfiguration(
            @PathVariable String cle,
            @Valid @RequestBody ConfigurationSysteme configuration) {
        configuration.setCle(cle);
        return ResponseEntity.ok(configurationService.saveConfiguration(configuration));
    }

    @DeleteMapping("/{cle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable String cle) {
        configurationService.deleteConfiguration(cle);
        return ResponseEntity.noContent().build();
    }
}
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.ConfigurationSystemeDTO;
import com.orleansgo.administrateur.service.ConfigurationSystemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationSystemeController {

    @Autowired
    private ConfigurationSystemeService configurationService;

    @GetMapping
    public ResponseEntity<List<ConfigurationSystemeDTO>> getAllConfigurations() {
        return ResponseEntity.ok(configurationService.getAllConfigurations());
    }

    @GetMapping("/{cle}")
    public ResponseEntity<ConfigurationSystemeDTO> getConfigurationByCle(@PathVariable String cle) {
        return ResponseEntity.ok(configurationService.getConfigurationByCle(cle));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ConfigurationSystemeDTO>> getConfigurationsByType(@PathVariable String type) {
        return ResponseEntity.ok(configurationService.getConfigurationsByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ConfigurationSystemeDTO>> searchConfigurations(@RequestParam String recherche) {
        return ResponseEntity.ok(configurationService.searchConfigurations(recherche));
    }

    @PostMapping
    public ResponseEntity<ConfigurationSystemeDTO> createConfiguration(@RequestBody ConfigurationSystemeDTO configDTO) {
        return new ResponseEntity<>(configurationService.createConfiguration(configDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{cle}")
    public ResponseEntity<ConfigurationSystemeDTO> updateConfiguration(@PathVariable String cle, @RequestBody ConfigurationSystemeDTO configDTO) {
        return ResponseEntity.ok(configurationService.updateConfiguration(cle, configDTO));
    }

    @DeleteMapping("/{cle}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable String cle) {
        configurationService.deleteConfiguration(cle);
        return ResponseEntity.noContent().build();
    }
}
