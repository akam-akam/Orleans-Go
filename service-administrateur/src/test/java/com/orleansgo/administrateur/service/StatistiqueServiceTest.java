
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
