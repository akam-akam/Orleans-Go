
package com.orleansgo.vehicule.service;

import com.orleansgo.vehicule.dto.VehiculeDTO;
import com.orleansgo.vehicule.exception.VehiculeNotFoundException;
import com.orleansgo.vehicule.model.StatutVehicule;
import com.orleansgo.vehicule.model.TypeVehicule;
import com.orleansgo.vehicule.model.Vehicule;
import com.orleansgo.vehicule.repository.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehiculeServiceTest {

    @Mock
    private VehiculeRepository vehiculeRepository;

    @InjectMocks
    private VehiculeService vehiculeService;

    private Vehicule vehicule;
    private VehiculeDTO vehiculeDTO;
    private UUID vehiculeId;
    private UUID conducteurId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        vehiculeId = UUID.randomUUID();
        conducteurId = UUID.randomUUID();
        
        vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
        vehicule.setMarque("Toyota");
        vehicule.setModele("Corolla");
        vehicule.setAnnee(2020);
        vehicule.setImmatriculation("AB-123-CD");
        vehicule.setConducteurId(conducteurId);
        vehicule.setType(TypeVehicule.STANDARD);
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        
        vehiculeDTO = new VehiculeDTO();
        vehiculeDTO.setId(vehiculeId);
        vehiculeDTO.setMarque("Toyota");
        vehiculeDTO.setModele("Corolla");
        vehiculeDTO.setAnnee(2020);
        vehiculeDTO.setImmatriculation("AB-123-CD");
        vehiculeDTO.setConducteurId(conducteurId);
        vehiculeDTO.setType(TypeVehicule.STANDARD);
        vehiculeDTO.setStatut(StatutVehicule.DISPONIBLE);
    }

    @Test
    public void testTrouverTousLesVehicules() {
        when(vehiculeRepository.findAll()).thenReturn(Arrays.asList(vehicule));
        
        List<Vehicule> result = vehiculeService.trouverTousLesVehicules();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(vehiculeRepository, times(1)).findAll();
    }

    @Test
    public void testTrouverVehiculeParId() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        
        Vehicule result = vehiculeService.trouverVehiculeParId(vehiculeId);
        
        assertNotNull(result);
        assertEquals(vehiculeId, result.getId());
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
    }

    @Test
    public void testTrouverVehiculeParId_NonTrouve() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());
        
        assertThrows(VehiculeNotFoundException.class, () -> {
            vehiculeService.trouverVehiculeParId(vehiculeId);
        });
        
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
    }

    @Test
    public void testEnregistrerVehicule() {
        when(vehiculeRepository.save(any(Vehicule.class))).thenReturn(vehicule);
        
        Vehicule result = vehiculeService.enregistrerVehicule(vehicule);
        
        assertNotNull(result);
        assertEquals(vehiculeId, result.getId());
        verify(vehiculeRepository, times(1)).save(vehicule);
    }

    @Test
    public void testSupprimerVehicule() {
        doNothing().when(vehiculeRepository).deleteById(vehiculeId);
        
        vehiculeService.supprimerVehicule(vehiculeId);
        
        verify(vehiculeRepository, times(1)).deleteById(vehiculeId);
    }

    @Test
    public void testChangerStatutVehicule() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeRepository.save(any(Vehicule.class))).thenReturn(vehicule);
        
        Vehicule result = vehiculeService.changerStatutVehicule(vehiculeId, StatutVehicule.EN_MAINTENANCE);
        
        assertNotNull(result);
        assertEquals(StatutVehicule.EN_MAINTENANCE, result.getStatut());
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
        verify(vehiculeRepository, times(1)).save(vehicule);
    }

    @Test
    public void testTrouverVehiculeParConducteur() {
        when(vehiculeRepository.findByConducteurId(conducteurId)).thenReturn(Arrays.asList(vehicule));
        
        List<Vehicule> result = vehiculeService.trouverVehiculeParConducteur(conducteurId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conducteurId, result.get(0).getConducteurId());
        verify(vehiculeRepository, times(1)).findByConducteurId(conducteurId);
    }

    @Test
    public void testTrouverVehiculeParType() {
        when(vehiculeRepository.findByType(TypeVehicule.STANDARD)).thenReturn(Arrays.asList(vehicule));
        
        List<Vehicule> result = vehiculeService.trouverVehiculeParType(TypeVehicule.STANDARD);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeVehicule.STANDARD, result.get(0).getType());
        verify(vehiculeRepository, times(1)).findByType(TypeVehicule.STANDARD);
    }

    @Test
    public void testTrouverVehiculeParStatut() {
        when(vehiculeRepository.findByStatut(StatutVehicule.DISPONIBLE)).thenReturn(Arrays.asList(vehicule));
        
        List<Vehicule> result = vehiculeService.trouverVehiculeParStatut(StatutVehicule.DISPONIBLE);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(StatutVehicule.DISPONIBLE, result.get(0).getStatut());
        verify(vehiculeRepository, times(1)).findByStatut(StatutVehicule.DISPONIBLE);
    }

    @Test
    public void testConvertToDTO() {
        VehiculeDTO result = vehiculeService.convertToDTO(vehicule);
        
        assertNotNull(result);
        assertEquals(vehicule.getId(), result.getId());
        assertEquals(vehicule.getMarque(), result.getMarque());
        assertEquals(vehicule.getModele(), result.getModele());
        assertEquals(vehicule.getType(), result.getType());
        assertEquals(vehicule.getStatut(), result.getStatut());
    }

    @Test
    public void testConvertToEntity() {
        Vehicule result = vehiculeService.convertToEntity(vehiculeDTO);
        
        assertNotNull(result);
        assertEquals(vehiculeDTO.getId(), result.getId());
        assertEquals(vehiculeDTO.getMarque(), result.getMarque());
        assertEquals(vehiculeDTO.getModele(), result.getModele());
        assertEquals(vehiculeDTO.getType(), result.getType());
        assertEquals(vehiculeDTO.getStatut(), result.getStatut());
    }
}
