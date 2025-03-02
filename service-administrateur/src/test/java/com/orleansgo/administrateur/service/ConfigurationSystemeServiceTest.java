
package com.orleansgo.administrateur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.ConfigurationSysteme;
import com.orleansgo.administrateur.repository.ConfigurationSystemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConfigurationSystemeServiceTest {

    @Mock
    private ConfigurationSystemeRepository configurationRepository;
    
    @Mock
    private AuditService auditService;
    
    @Mock
    private SecurityContext securityContext;
    
    @InjectMocks
    private ConfigurationSystemeService configurationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "admin", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllConfigurations() {
        // Arrange
        ConfigurationSysteme config1 = new ConfigurationSysteme();
        ConfigurationSysteme config2 = new ConfigurationSysteme();
        List<ConfigurationSysteme> expectedConfigs = Arrays.asList(config1, config2);
        
        when(configurationRepository.findAll()).thenReturn(expectedConfigs);

        // Act
        List<ConfigurationSysteme> result = configurationService.getAllConfigurations();

        // Assert
        assertEquals(expectedConfigs, result);
        verify(configurationRepository, times(1)).findAll();
    }

    @Test
    public void testGetConfigurationsByType() {
        // Arrange
        ConfigurationSysteme.TypeConfiguration type = ConfigurationSysteme.TypeConfiguration.PAIEMENT;
        ConfigurationSysteme config1 = new ConfigurationSysteme();
        List<ConfigurationSysteme> expectedConfigs = Collections.singletonList(config1);
        
        when(configurationRepository.findByType(type)).thenReturn(expectedConfigs);

        // Act
        List<ConfigurationSysteme> result = configurationService.getConfigurationsByType(type);

        // Assert
        assertEquals(expectedConfigs, result);
        verify(configurationRepository, times(1)).findByType(type);
    }

    @Test
    public void testGetConfigurationByCle_Found() {
        // Arrange
        String cle = "COMMISSION_RATE";
        ConfigurationSysteme expectedConfig = new ConfigurationSysteme();
        expectedConfig.setCle(cle);
        
        when(configurationRepository.findById(cle)).thenReturn(Optional.of(expectedConfig));

        // Act
        ConfigurationSysteme result = configurationService.getConfigurationByCle(cle);

        // Assert
        assertEquals(expectedConfig, result);
        verify(configurationRepository, times(1)).findById(cle);
    }

    @Test
    public void testGetConfigurationByCle_NotFound() {
        // Arrange
        String cle = "UNKNOWN_KEY";
        
        when(configurationRepository.findById(cle)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            configurationService.getConfigurationByCle(cle);
        });
        verify(configurationRepository, times(1)).findById(cle);
    }

    @Test
    public void testSaveConfiguration_New() {
        // Arrange
        ConfigurationSysteme config = new ConfigurationSysteme();
        config.setCle("NEW_KEY");
        config.setDescription("Nouvelle configuration");
        config.setType(ConfigurationSysteme.TypeConfiguration.SYSTEME);
        
        when(configurationRepository.existsById("NEW_KEY")).thenReturn(false);
        when(configurationRepository.save(any(ConfigurationSysteme.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(auditService).logAction(anyString(), anyString(), anyString(), anyString(), anyString());

        // Act
        ConfigurationSysteme result = configurationService.saveConfiguration(config);

        // Assert
        assertNotNull(result);
        assertEquals("NEW_KEY", result.getCle());
        assertEquals("admin", result.getModifiePar());
        assertNotNull(result.getDerniereModification());
        verify(configurationRepository, times(1)).existsById("NEW_KEY");
        verify(configurationRepository, times(1)).save(any(ConfigurationSysteme.class));
        verify(auditService, times(1)).logAction(eq("CONFIGURATION_CREEE"), eq("CONFIGURATION"), eq("NEW_KEY"), eq("admin"), anyString());
    }

    @Test
    public void testDeleteConfiguration() {
        // Arrange
        String cle = "KEY_TO_DELETE";
        ConfigurationSysteme config = new ConfigurationSysteme();
        config.setCle(cle);
        config.setDescription("Configuration à supprimer");
        
        when(configurationRepository.findById(cle)).thenReturn(Optional.of(config));
        doNothing().when(configurationRepository).delete(any(ConfigurationSysteme.class));
        doNothing().when(auditService).logAction(anyString(), anyString(), anyString(), anyString(), anyString());

        // Act
        configurationService.deleteConfiguration(cle);

        // Assert
        verify(configurationRepository, times(1)).findById(cle);
        verify(configurationRepository, times(1)).delete(any(ConfigurationSysteme.class));
        verify(auditService, times(1)).logAction(eq("CONFIGURATION_SUPPRIMEE"), eq("CONFIGURATION"), eq(cle), eq("admin"), anyString());
    }
}
