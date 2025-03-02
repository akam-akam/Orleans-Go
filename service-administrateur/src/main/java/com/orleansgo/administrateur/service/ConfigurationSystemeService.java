
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.ConfigurationSysteme;
import com.orleansgo.administrateur.repository.ConfigurationSystemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigurationSystemeService {

    private final ConfigurationSystemeRepository configurationRepository;
    private final AuditService auditService;

    /**
     * Récupère toutes les configurations
     */
    @Transactional(readOnly = true)
    public List<ConfigurationSysteme> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    /**
     * Récupère les configurations par type
     */
    @Transactional(readOnly = true)
    public List<ConfigurationSysteme> getConfigurationsByType(ConfigurationSysteme.TypeConfiguration type) {
        return configurationRepository.findByType(type);
    }

    /**
     * Récupère une configuration par sa clé
     */
    @Transactional(readOnly = true)
    public ConfigurationSysteme getConfigurationByCle(String cle) {
        return configurationRepository.findById(cle)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration non trouvée avec la clé: " + cle));
    }

    /**
     * Crée ou met à jour une configuration
     */
    @Transactional
    public ConfigurationSysteme saveConfiguration(ConfigurationSysteme configuration) {
        String username = getCurrentUsername();
        boolean isNew = !configurationRepository.existsById(configuration.getCle());
        
        configuration.setDerniereModification(LocalDateTime.now());
        configuration.setModifiePar(username);
        
        ConfigurationSysteme savedConfig = configurationRepository.save(configuration);
        
        // Audit de l'action
        auditService.logAction(
            isNew ? "CONFIGURATION_CREEE" : "CONFIGURATION_MODIFIEE",
            "CONFIGURATION",
            savedConfig.getCle(),
            username,
            "Configuration " + (isNew ? "créée" : "modifiée") + ": " + savedConfig.getDescription()
        );
        
        return savedConfig;
    }

    /**
     * Supprime une configuration
     */
    @Transactional
    public void deleteConfiguration(String cle) {
        ConfigurationSysteme config = getConfigurationByCle(cle);
        configurationRepository.delete(config);
        
        // Audit de l'action
        auditService.logAction(
            "CONFIGURATION_SUPPRIMEE",
            "CONFIGURATION",
            cle,
            getCurrentUsername(),
            "Configuration supprimée: " + config.getDescription()
        );
    }

    /**
     * Récupère le nom d'utilisateur courant
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.ConfigurationSystemeDTO;
import com.orleansgo.administrateur.model.ConfigurationSysteme;
import com.orleansgo.administrateur.repository.ConfigurationSystemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigurationSystemeService {

    @Autowired
    private ConfigurationSystemeRepository configurationRepository;

    public List<ConfigurationSystemeDTO> getAllConfigurations() {
        return configurationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConfigurationSystemeDTO getConfigurationByCle(String cle) {
        ConfigurationSysteme config = configurationRepository.findById(cle)
                .orElseThrow(() -> new RuntimeException("Configuration non trouvée avec la clé: " + cle));
        return convertToDTO(config);
    }

    public List<ConfigurationSystemeDTO> getConfigurationsByType(String type) {
        return configurationRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ConfigurationSystemeDTO> searchConfigurations(String recherche) {
        return configurationRepository.findByDescriptionContainingIgnoreCase(recherche).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConfigurationSystemeDTO createConfiguration(ConfigurationSystemeDTO configDTO) {
        if (configurationRepository.existsById(configDTO.getCle())) {
            throw new RuntimeException("Une configuration avec cette clé existe déjà: " + configDTO.getCle());
        }
        ConfigurationSysteme config = convertToEntity(configDTO);
        config = configurationRepository.save(config);
        return convertToDTO(config);
    }

    public ConfigurationSystemeDTO updateConfiguration(String cle, ConfigurationSystemeDTO configDTO) {
        if (!configurationRepository.existsById(cle)) {
            throw new RuntimeException("Configuration non trouvée avec la clé: " + cle);
        }
        ConfigurationSysteme config = convertToEntity(configDTO);
        config.setCle(cle);
        config = configurationRepository.save(config);
        return convertToDTO(config);
    }

    public void deleteConfiguration(String cle) {
        if (!configurationRepository.existsById(cle)) {
            throw new RuntimeException("Configuration non trouvée avec la clé: " + cle);
        }
        configurationRepository.deleteById(cle);
    }

    // Méthodes utilitaires
    
    private ConfigurationSystemeDTO convertToDTO(ConfigurationSysteme config) {
        ConfigurationSystemeDTO dto = new ConfigurationSystemeDTO();
        dto.setCle(config.getCle());
        dto.setValeur(config.getValeur());
        dto.setDescription(config.getDescription());
        dto.setType(config.getType());
        dto.setDateCreation(config.getDateCreation());
        dto.setDateModification(config.getDateModification());
        dto.setModifiePar(config.getModifiePar());
        return dto;
    }

    private ConfigurationSysteme convertToEntity(ConfigurationSystemeDTO dto) {
        ConfigurationSysteme config = new ConfigurationSysteme();
        config.setCle(dto.getCle());
        config.setValeur(dto.getValeur());
        config.setDescription(dto.getDescription());
        config.setType(dto.getType());
        config.setDateCreation(dto.getDateCreation());
        config.setDateModification(dto.getDateModification());
        config.setModifiePar(dto.getModifiePar());
        return config;
    }
}
