
package com.orleansgo.administrateur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;

public class StatistiqueServiceTest {

    @Mock
    private RapportService rapportService;

    @InjectMocks
    private StatistiqueService statistiqueService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStatistiquesCourses() {
        // Arrange
        LocalDate debut = LocalDate.of(2025, 3, 1);
        LocalDate fin = LocalDate.of(2025, 3, 3);

        // Act
        Map<String, Object> result = statistiqueService.getStatistiquesCourses(debut, fin);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("nombreTotalCourses"));
        assertTrue(result.containsKey("coursesParJour"));
        assertTrue(result.containsKey("revenuTotal"));
        assertTrue(result.containsKey("commissionsPercues"));
        assertEquals(1250, result.get("nombreTotalCourses"));
    }

    @Test
    public void testGetStatistiquesChauffeurs() {
        // Act
        Map<String, Object> result = statistiqueService.getStatistiquesChauffeurs();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("nombreTotalChauffeurs"));
        assertTrue(result.containsKey("chauffeursActifs"));
        assertTrue(result.containsKey("nouvellementInscrits"));
        assertTrue(result.containsKey("evaluationMoyenne"));
        assertEquals(85, result.get("nombreTotalChauffeurs"));
    }

    @Test
    public void testGetStatistiquesUtilisateurs() {
        // Act
        Map<String, Object> result = statistiqueService.getStatistiquesUtilisateurs();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("nombreTotalUtilisateurs"));
        assertTrue(result.containsKey("utilisateursActifsMois"));
        assertTrue(result.containsKey("nouveauxUtilisateurs"));
        assertEquals(1250, result.get("nombreTotalUtilisateurs"));
    }

    @Test
    public void testGenererRapportStatistiques() {
        // Arrange
        doNothing().when(rapportService).creerRapportStatistique(anyString(), anyMap());

        // Act
        statistiqueService.genererRapportStatistiques();

        // Assert
        verify(rapportService, times(1)).creerRapportStatistique(anyString(), anyMap());
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.StatistiqueDTO;
import com.orleansgo.administrateur.model.Statistique;
import com.orleansgo.administrateur.repository.StatistiqueRepository;
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

class StatistiqueServiceTest {

    @Mock
    private StatistiqueRepository statistiqueRepository;

    @InjectMocks
    private StatistiqueService statistiqueService;

    private Statistique statistique;
    private StatistiqueDTO statistiqueDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        statistique = new Statistique();
        statistique.setId(1L);
        statistique.setType("COURSE");
        statistique.setLabel("Nombre de courses");
        statistique.setValeur(100.0);
        statistique.setDateCreation(LocalDateTime.now());
        statistique.setPeriode(LocalDateTime.now());
        statistique.setMetadonnees("{\"region\":\"Orleans\"}");

        statistiqueDTO = new StatistiqueDTO();
        statistiqueDTO.setId(1L);
        statistiqueDTO.setType("COURSE");
        statistiqueDTO.setLabel("Nombre de courses");
        statistiqueDTO.setValeur(100.0);
        statistiqueDTO.setDateCreation(LocalDateTime.now());
        statistiqueDTO.setPeriode(LocalDateTime.now());
        statistiqueDTO.setMetadonnees("{\"region\":\"Orleans\"}");
    }

    @Test
    void testGetAllStatistiques() {
        List<Statistique> statistiques = new ArrayList<>();
        statistiques.add(statistique);
        
        when(statistiqueRepository.findAll()).thenReturn(statistiques);
        
        List<StatistiqueDTO> result = statistiqueService.getAllStatistiques();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statistiqueDTO.getId(), result.get(0).getId());
        assertEquals(statistiqueDTO.getType(), result.get(0).getType());
        assertEquals(statistiqueDTO.getLabel(), result.get(0).getLabel());
        
        verify(statistiqueRepository, times(1)).findAll();
    }

    @Test
    void testGetStatistiqueById() {
        when(statistiqueRepository.findById(1L)).thenReturn(Optional.of(statistique));
        
        StatistiqueDTO result = statistiqueService.getStatistiqueById(1L);
        
        assertNotNull(result);
        assertEquals(statistiqueDTO.getId(), result.getId());
        assertEquals(statistiqueDTO.getType(), result.getType());
        assertEquals(statistiqueDTO.getLabel(), result.getLabel());
        
        verify(statistiqueRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStatistiquesByType() {
        List<Statistique> statistiques = new ArrayList<>();
        statistiques.add(statistique);
        
        when(statistiqueRepository.findByType("COURSE")).thenReturn(statistiques);
        
        List<StatistiqueDTO> result = statistiqueService.getStatistiquesByType("COURSE");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statistiqueDTO.getId(), result.get(0).getId());
        assertEquals(statistiqueDTO.getType(), result.get(0).getType());
        
        verify(statistiqueRepository, times(1)).findByType("COURSE");
    }

    @Test
    void testCreateStatistique() {
        when(statistiqueRepository.save(any(Statistique.class))).thenReturn(statistique);
        
        StatistiqueDTO result = statistiqueService.createStatistique(statistiqueDTO);
        
        assertNotNull(result);
        assertEquals(statistiqueDTO.getId(), result.getId());
        assertEquals(statistiqueDTO.getType(), result.getType());
        
        verify(statistiqueRepository, times(1)).save(any(Statistique.class));
    }

    @Test
    void testUpdateStatistique() {
        when(statistiqueRepository.existsById(1L)).thenReturn(true);
        when(statistiqueRepository.save(any(Statistique.class))).thenReturn(statistique);
        
        StatistiqueDTO result = statistiqueService.updateStatistique(1L, statistiqueDTO);
        
        assertNotNull(result);
        assertEquals(statistiqueDTO.getId(), result.getId());
        assertEquals(statistiqueDTO.getType(), result.getType());
        
        verify(statistiqueRepository, times(1)).existsById(1L);
        verify(statistiqueRepository, times(1)).save(any(Statistique.class));
    }

    @Test
    void testDeleteStatistique() {
        when(statistiqueRepository.existsById(1L)).thenReturn(true);
        doNothing().when(statistiqueRepository).deleteById(1L);
        
        statistiqueService.deleteStatistique(1L);
        
        verify(statistiqueRepository, times(1)).existsById(1L);
        verify(statistiqueRepository, times(1)).deleteById(1L);
    }
}
