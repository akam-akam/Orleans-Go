
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.RapportDTO;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Rapport;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.RapportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RapportServiceTest {

    @Mock
    private RapportRepository rapportRepository;

    @Mock
    private AdministrateurRepository administrateurRepository;

    @InjectMocks
    private RapportService rapportService;

    private Rapport rapport;
    private RapportDTO rapportDTO;
    private Administrateur admin;
    private UUID rapportId;
    private UUID adminId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminId = UUID.randomUUID();
        rapportId = UUID.randomUUID();
        
        admin = new Administrateur();
        admin.setId(adminId);
        admin.setEmail("admin@orleansgo.com");
        
        rapport = new Rapport();
        rapport.setId(rapportId);
        rapport.setTitre("Rapport mensuel");
        rapport.setDescription("Statistiques des courses");
        rapport.setTypeDonnees("COURSES");
        rapport.setPeriodeDu(LocalDateTime.now().minusMonths(1));
        rapport.setPeriodeAu(LocalDateTime.now());
        rapport.setContenuJson("{\"courses\": 150, \"revenu\": 3000}");
        rapport.setActif(true);
        rapport.setCreePar(admin);
        rapport.setDateCreation(LocalDateTime.now());
        
        rapportDTO = new RapportDTO();
        rapportDTO.setId(rapportId);
        rapportDTO.setTitre("Rapport mensuel");
        rapportDTO.setDescription("Statistiques des courses");
        rapportDTO.setTypeDonnees("COURSES");
        rapportDTO.setPeriodeDu(LocalDateTime.now().minusMonths(1));
        rapportDTO.setPeriodeAu(LocalDateTime.now());
        rapportDTO.setContenuJson("{\"courses\": 150, \"revenu\": 3000}");
        rapportDTO.setActif(true);
        rapportDTO.setCreeParId(adminId);
        rapportDTO.setDateCreation(LocalDateTime.now());
    }

    @Test
    void getAllRapports() {
        when(rapportRepository.findAll()).thenReturn(Arrays.asList(rapport));
        
        List<RapportDTO> result = rapportService.getAllRapports();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rapport.getId(), result.get(0).getId());
        verify(rapportRepository, times(1)).findAll();
    }

    @Test
    void getRapportById() {
        when(rapportRepository.findById(rapportId)).thenReturn(Optional.of(rapport));
        
        Optional<RapportDTO> result = rapportService.getRapportById(rapportId);
        
        assertTrue(result.isPresent());
        assertEquals(rapport.getId(), result.get().getId());
        verify(rapportRepository, times(1)).findById(rapportId);
    }

    @Test
    void createRapport() {
        when(administrateurRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(rapportRepository.save(any(Rapport.class))).thenReturn(rapport);
        
        RapportDTO result = rapportService.createRapport(rapportDTO);
        
        assertNotNull(result);
        assertEquals(rapport.getId(), result.getId());
        verify(rapportRepository, times(1)).save(any(Rapport.class));
    }

    @Test
    void updateRapport() {
        when(rapportRepository.existsById(rapportId)).thenReturn(true);
        when(administrateurRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(rapportRepository.save(any(Rapport.class))).thenReturn(rapport);
        
        Optional<RapportDTO> result = rapportService.updateRapport(rapportId, rapportDTO);
        
        assertTrue(result.isPresent());
        assertEquals(rapport.getId(), result.get().getId());
        verify(rapportRepository, times(1)).save(any(Rapport.class));
    }

    @Test
    void deleteRapport() {
        when(rapportRepository.existsById(rapportId)).thenReturn(true);
        doNothing().when(rapportRepository).deleteById(rapportId);
        
        boolean result = rapportService.deleteRapport(rapportId);
        
        assertTrue(result);
        verify(rapportRepository, times(1)).deleteById(rapportId);
    }

    @Test
    void getRapportsByPeriode() {
        LocalDateTime debut = LocalDateTime.now().minusMonths(2);
        LocalDateTime fin = LocalDateTime.now();
        
        when(rapportRepository.findByPeriodeDuBetween(debut, fin)).thenReturn(Arrays.asList(rapport));
        
        List<RapportDTO> result = rapportService.getRapportsByPeriode(debut, fin);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rapport.getId(), result.get(0).getId());
        verify(rapportRepository, times(1)).findByPeriodeDuBetween(debut, fin);
    }
}
