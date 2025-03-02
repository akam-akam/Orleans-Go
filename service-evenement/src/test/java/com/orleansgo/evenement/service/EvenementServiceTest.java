
package com.orleansgo.evenement.service;

import com.orleansgo.evenement.dto.EvenementDTO;
import com.orleansgo.evenement.model.Evenement;
import com.orleansgo.evenement.model.StatutEvenement;
import com.orleansgo.evenement.repository.EvenementRepository;
import com.orleansgo.evenement.repository.InscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EvenementServiceTest {

    @Mock
    private EvenementRepository evenementRepository;

    @Mock
    private InscriptionRepository inscriptionRepository;

    @InjectMocks
    private EvenementService evenementService;

    private Evenement evenement;
    private EvenementDTO evenementDTO;

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();
        Set<String> categories = new HashSet<>();
        categories.add("Communautaire");
        
        evenement = new Evenement();
        evenement.setId(1L);
        evenement.setTitre("Événement test");
        evenement.setDescription("Description de test");
        evenement.setDateDebut(now.plusDays(1));
        evenement.setDateFin(now.plusDays(1).plusHours(2));
        evenement.setLieu("Orléans Centre");
        evenement.setCategories(categories);
        evenement.setStatut(StatutEvenement.PLANIFIE);
        evenement.setPublicationActive(true);
        evenement.setCreateurId(1L);
        evenement.setCapaciteMax(100);
        evenement.setNombreInscrits(0);
        
        evenementDTO = new EvenementDTO();
        evenementDTO.setTitre("Événement test");
        evenementDTO.setDescription("Description de test");
        evenementDTO.setDateDebut(now.plusDays(1));
        evenementDTO.setDateFin(now.plusDays(1).plusHours(2));
        evenementDTO.setLieu("Orléans Centre");
        evenementDTO.setCategories(categories);
        evenementDTO.setPublicationActive(true);
        evenementDTO.setCreateurId(1L);
        evenementDTO.setCapaciteMax(100);
    }

    @Test
    public void testGetAllEvenements() {
        when(evenementRepository.findAll()).thenReturn(Arrays.asList(evenement));
        
        List<EvenementDTO> result = evenementService.getAllEvenements();
        
        assertEquals(1, result.size());
        assertEquals(evenement.getTitre(), result.get(0).getTitre());
    }

    @Test
    public void testGetEvenementById() {
        when(evenementRepository.findById(1L)).thenReturn(Optional.of(evenement));
        
        EvenementDTO result = evenementService.getEvenementById(1L);
        
        assertNotNull(result);
        assertEquals(evenement.getTitre(), result.getTitre());
    }

    @Test
    public void testCreateEvenement() {
        when(evenementRepository.save(any(Evenement.class))).thenReturn(evenement);
        
        EvenementDTO result = evenementService.createEvenement(evenementDTO);
        
        assertNotNull(result);
        assertEquals(evenement.getTitre(), result.getTitre());
    }

    @Test
    public void testUpdateEvenement() {
        when(evenementRepository.findById(1L)).thenReturn(Optional.of(evenement));
        when(evenementRepository.save(any(Evenement.class))).thenReturn(evenement);
        
        EvenementDTO result = evenementService.updateEvenement(1L, evenementDTO);
        
        assertNotNull(result);
        assertEquals(evenement.getTitre(), result.getTitre());
    }

    @Test
    public void testGetEvenementsByStatut() {
        when(evenementRepository.findByStatut(StatutEvenement.PLANIFIE)).thenReturn(Arrays.asList(evenement));
        
        List<EvenementDTO> result = evenementService.getEvenementsByStatut(StatutEvenement.PLANIFIE);
        
        assertEquals(1, result.size());
        assertEquals(evenement.getTitre(), result.get(0).getTitre());
    }

    @Test
    public void testGetEvenementsPublics() {
        when(evenementRepository.findByPublicationActiveTrue()).thenReturn(Arrays.asList(evenement));
        
        List<EvenementDTO> result = evenementService.getEvenementsPublics();
        
        assertEquals(1, result.size());
        assertEquals(evenement.getTitre(), result.get(0).getTitre());
    }

    @Test
    public void testSearchEvenements() {
        Page<Evenement> page = new PageImpl<>(Arrays.asList(evenement));
        when(evenementRepository.findByTitreContainingIgnoreCase(any(String.class), any(Pageable.class))).thenReturn(page);
        
        Page<EvenementDTO> result = evenementService.searchEvenements("test", Pageable.unpaged());
        
        assertEquals(1, result.getContent().size());
        assertEquals(evenement.getTitre(), result.getContent().get(0).getTitre());
    }
}
