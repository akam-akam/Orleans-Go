
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
