
package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.exception.ConducteurNotFoundException;
import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.model.StatutConducteur;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import com.orleansgo.conducteur.repository.DocumentRepository;
import com.orleansgo.conducteur.repository.VehiculeConducteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConducteurServiceTest {

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private VehiculeConducteurRepository vehiculeConducteurRepository;

    @InjectMocks
    private ConducteurService conducteurService;

    private Conducteur conducteur;
    private ConducteurDTO conducteurDTO;

    @BeforeEach
    void setUp() {
        conducteur = Conducteur.builder()
                .id(1L)
                .utilisateurId(10L)
                .numeroPermis("12345678")
                .dateExpirationPermis(LocalDateTime.now().plusYears(5))
                .documentsValides(true)
                .disponible(true)
                .statut(StatutConducteur.ACTIF)
                .latitude(48.8566)
                .longitude(2.3522)
                .dernierePosition(LocalDateTime.now())
                .noteGlobale(4.5)
                .nombreCourses(100)
                .nombreAvis(80)
                .documents(Collections.emptySet())
                .vehicules(Collections.emptySet())
                .dateCreation(LocalDateTime.now())
                .dateModification(LocalDateTime.now())
                .build();

        conducteurDTO = ConducteurDTO.builder()
                .id(1L)
                .utilisateurId(10L)
                .numeroPermis("12345678")
                .dateExpirationPermis(LocalDateTime.now().plusYears(5))
                .documentsValides(true)
                .disponible(true)
                .statut(StatutConducteur.ACTIF)
                .latitude(48.8566)
                .longitude(2.3522)
                .dernierePosition(LocalDateTime.now())
                .noteGlobale(4.5)
                .nombreCourses(100)
                .nombreAvis(80)
                .documents(Collections.emptySet())
                .vehicules(Collections.emptySet())
                .build();
    }

    @Test
    void testGetAllConducteurs() {
        when(conducteurRepository.findAll()).thenReturn(List.of(conducteur));
        
        List<ConducteurDTO> result = conducteurService.getAllConducteurs();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conducteur.getId(), result.get(0).getId());
        assertEquals(conducteur.getUtilisateurId(), result.get(0).getUtilisateurId());
    }

    @Test
    void testGetConducteurById() {
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        
        ConducteurDTO result = conducteurService.getConducteurById(1L);
        
        assertNotNull(result);
        assertEquals(conducteur.getId(), result.getId());
        assertEquals(conducteur.getUtilisateurId(), result.getUtilisateurId());
    }

    @Test
    void testGetConducteurById_NotFound() {
        when(conducteurRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(ConducteurNotFoundException.class, () -> conducteurService.getConducteurById(99L));
    }

    @Test
    void testCreateConducteur() {
        when(conducteurRepository.save(any(Conducteur.class))).thenReturn(conducteur);
        
        ConducteurDTO result = conducteurService.createConducteur(conducteurDTO);
        
        assertNotNull(result);
        assertEquals(conducteur.getId(), result.getId());
        assertEquals(conducteur.getUtilisateurId(), result.getUtilisateurId());
        
        verify(conducteurRepository, times(1)).save(any(Conducteur.class));
    }

    @Test
    void testUpdateConducteur() {
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        when(conducteurRepository.save(any(Conducteur.class))).thenReturn(conducteur);
        
        ConducteurDTO updatedDTO = ConducteurDTO.builder()
                .id(1L)
                .numeroPermis("87654321")
                .disponible(false)
                .build();
        
        ConducteurDTO result = conducteurService.updateConducteur(1L, updatedDTO);
        
        assertNotNull(result);
        verify(conducteurRepository, times(1)).findById(1L);
        verify(conducteurRepository, times(1)).save(any(Conducteur.class));
    }

    @Test
    void testUpdateStatut() {
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        when(conducteurRepository.save(any(Conducteur.class))).thenReturn(conducteur);
        
        ConducteurDTO result = conducteurService.updateStatut(1L, StatutConducteur.INACTIF);
        
        assertNotNull(result);
        verify(conducteurRepository, times(1)).findById(1L);
        verify(conducteurRepository, times(1)).save(any(Conducteur.class));
    }

    @Test
    void testUpdatePosition() {
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        when(conducteurRepository.save(any(Conducteur.class))).thenReturn(conducteur);
        
        ConducteurDTO result = conducteurService.updatePosition(1L, 45.7640, 4.8357);
        
        assertNotNull(result);
        verify(conducteurRepository, times(1)).findById(1L);
        verify(conducteurRepository, times(1)).save(any(Conducteur.class));
    }
}
