
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
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.ConfigurationSystemeDTO;
import com.orleansgo.administrateur.model.ConfigurationSysteme;
import com.orleansgo.administrateur.repository.ConfigurationSystemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConfigurationSystemeServiceTest {

    @Mock
    private ConfigurationSystemeRepository configurationRepository;

    @InjectMocks
    private ConfigurationSystemeService configurationService;

    private ConfigurationSysteme configuration;
    private ConfigurationSystemeDTO configurationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();
        
        configuration = new ConfigurationSysteme();
        configuration.setCle("TARIF_BASE_COURSE");
        configuration.setValeur("5.0");
        configuration.setDescription("Tarif de base pour une course");
        configuration.setType("COURSE");
        configuration.setDateCreation(now);
        configuration.setDateModification(now);
        configuration.setModifiePar("admin");

        configurationDTO = new ConfigurationSystemeDTO();
        configurationDTO.setCle("TARIF_BASE_COURSE");
        configurationDTO.setValeur("5.0");
        configurationDTO.setDescription("Tarif de base pour une course");
        configurationDTO.setType("COURSE");
        configurationDTO.setDateCreation(now);
        configurationDTO.setDateModification(now);
        configurationDTO.setModifiePar("admin");
    }

    @Test
    void testGetAllConfigurations() {
        List<ConfigurationSysteme> configurations = new ArrayList<>();
        configurations.add(configuration);
        
        when(configurationRepository.findAll()).thenReturn(configurations);
        
        List<ConfigurationSystemeDTO> result = configurationService.getAllConfigurations();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(configurationDTO.getCle(), result.get(0).getCle());
        assertEquals(configurationDTO.getValeur(), result.get(0).getValeur());
        
        verify(configurationRepository, times(1)).findAll();
    }

    @Test
    void testGetConfigurationByCle() {
        when(configurationRepository.findById("TARIF_BASE_COURSE")).thenReturn(Optional.of(configuration));
        
        ConfigurationSystemeDTO result = configurationService.getConfigurationByCle("TARIF_BASE_COURSE");
        
        assertNotNull(result);
        assertEquals(configurationDTO.getCle(), result.getCle());
        assertEquals(configurationDTO.getValeur(), result.getValeur());
        
        verify(configurationRepository, times(1)).findById("TARIF_BASE_COURSE");
    }

    @Test
    void testGetConfigurationsByType() {
        List<ConfigurationSysteme> configurations = new ArrayList<>();
        configurations.add(configuration);
        
        when(configurationRepository.findByType("COURSE")).thenReturn(configurations);
        
        List<ConfigurationSystemeDTO> result = configurationService.getConfigurationsByType("COURSE");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(configurationDTO.getCle(), result.get(0).getCle());
        assertEquals(configurationDTO.getType(), result.get(0).getType());
        
        verify(configurationRepository, times(1)).findByType("COURSE");
    }

    @Test
    void testCreateConfiguration() {
        when(configurationRepository.existsById("TARIF_BASE_COURSE")).thenReturn(false);
        when(configurationRepository.save(any(ConfigurationSysteme.class))).thenReturn(configuration);
        
        ConfigurationSystemeDTO result = configurationService.createConfiguration(configurationDTO);
        
        assertNotNull(result);
        assertEquals(configurationDTO.getCle(), result.getCle());
        assertEquals(configurationDTO.getValeur(), result.getValeur());
        
        verify(configurationRepository, times(1)).existsById("TARIF_BASE_COURSE");
        verify(configurationRepository, times(1)).save(any(ConfigurationSysteme.class));
    }

    @Test
    void testUpdateConfiguration() {
        when(configurationRepository.existsById("TARIF_BASE_COURSE")).thenReturn(true);
        when(configurationRepository.save(any(ConfigurationSysteme.class))).thenReturn(configuration);
        
        ConfigurationSystemeDTO result = configurationService.updateConfiguration("TARIF_BASE_COURSE", configurationDTO);
        
        assertNotNull(result);
        assertEquals(configurationDTO.getCle(), result.getCle());
        assertEquals(configurationDTO.getValeur(), result.getValeur());
        
        verify(configurationRepository, times(1)).existsById("TARIF_BASE_COURSE");
        verify(configurationRepository, times(1)).save(any(ConfigurationSysteme.class));
    }

    @Test
    void testDeleteConfiguration() {
        when(configurationRepository.existsById("TARIF_BASE_COURSE")).thenReturn(true);
        doNothing().when(configurationRepository).deleteById("TARIF_BASE_COURSE");
        
        configurationService.deleteConfiguration("TARIF_BASE_COURSE");
        
        verify(configurationRepository, times(1)).existsById("TARIF_BASE_COURSE");
        verify(configurationRepository, times(1)).deleteById("TARIF_BASE_COURSE");
    }
}
