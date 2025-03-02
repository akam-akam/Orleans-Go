
package com.orleansgo.commission.service;

import com.orleansgo.commission.dto.CommissionDTO;
import com.orleansgo.commission.exception.CommissionNotFoundException;
import com.orleansgo.commission.model.Commission;
import com.orleansgo.commission.model.TypeCommission;
import com.orleansgo.commission.repository.CommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommissionServiceTest {

    @Mock
    private CommissionRepository commissionRepository;

    @InjectMocks
    private CommissionService commissionService;

    private Commission commission;
    private CommissionDTO commissionDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        commission = new Commission();
        commission.setId(1L);
        commission.setNom("Commission Standard");
        commission.setDescription("Commission standard pour tous les trajets");
        commission.setTaux(BigDecimal.valueOf(15.0));
        commission.setMontantFixe(BigDecimal.valueOf(1.5));
        commission.setType(TypeCommission.POURCENTAGE);
        commission.setDateDebut(now);
        commission.setActive(true);
        commission.setCreatedAt(now);
        
        commissionDTO = new CommissionDTO();
        commissionDTO.setId(1L);
        commissionDTO.setNom("Commission Standard");
        commissionDTO.setDescription("Commission standard pour tous les trajets");
        commissionDTO.setTaux(BigDecimal.valueOf(15.0));
        commissionDTO.setMontantFixe(BigDecimal.valueOf(1.5));
        commissionDTO.setType(TypeCommission.POURCENTAGE);
        commissionDTO.setDateDebut(now);
        commissionDTO.setActive(true);
    }

    @Test
    void getAllCommissions() {
        when(commissionRepository.findAll()).thenReturn(Arrays.asList(commission));
        
        List<CommissionDTO> result = commissionService.getAllCommissions();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commission.getId(), result.get(0).getId());
        assertEquals(commission.getNom(), result.get(0).getNom());
        verify(commissionRepository, times(1)).findAll();
    }

    @Test
    void getActiveCommissions() {
        when(commissionRepository.findActiveCommissions(any(LocalDateTime.class))).thenReturn(Arrays.asList(commission));
        
        List<CommissionDTO> result = commissionService.getActiveCommissions();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commission.getId(), result.get(0).getId());
        assertEquals(commission.getNom(), result.get(0).getNom());
        verify(commissionRepository, times(1)).findActiveCommissions(any(LocalDateTime.class));
    }

    @Test
    void getCommissionById() {
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        
        CommissionDTO result = commissionService.getCommissionById(1L);
        
        assertNotNull(result);
        assertEquals(commission.getId(), result.getId());
        assertEquals(commission.getNom(), result.getNom());
        verify(commissionRepository, times(1)).findById(1L);
    }

    @Test
    void getCommissionById_NotFound() {
        when(commissionRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(CommissionNotFoundException.class, () -> {
            commissionService.getCommissionById(99L);
        });
        
        verify(commissionRepository, times(1)).findById(99L);
    }

    @Test
    void createCommission() {
        when(commissionRepository.save(any(Commission.class))).thenReturn(commission);
        
        CommissionDTO result = commissionService.createCommission(commissionDTO);
        
        assertNotNull(result);
        assertEquals(commission.getId(), result.getId());
        assertEquals(commission.getNom(), result.getNom());
        verify(commissionRepository, times(1)).save(any(Commission.class));
    }

    @Test
    void updateCommission() {
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenReturn(commission);
        
        CommissionDTO result = commissionService.updateCommission(1L, commissionDTO);
        
        assertNotNull(result);
        assertEquals(commission.getId(), result.getId());
        assertEquals(commission.getNom(), result.getNom());
        verify(commissionRepository, times(1)).findById(1L);
        verify(commissionRepository, times(1)).save(any(Commission.class));
    }

    @Test
    void updateCommission_NotFound() {
        when(commissionRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(CommissionNotFoundException.class, () -> {
            commissionService.updateCommission(99L, commissionDTO);
        });
        
        verify(commissionRepository, times(1)).findById(99L);
        verify(commissionRepository, never()).save(any(Commission.class));
    }

    @Test
    void deleteCommission() {
        when(commissionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(commissionRepository).deleteById(1L);
        
        commissionService.deleteCommission(1L);
        
        verify(commissionRepository, times(1)).existsById(1L);
        verify(commissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCommission_NotFound() {
        when(commissionRepository.existsById(99L)).thenReturn(false);
        
        assertThrows(CommissionNotFoundException.class, () -> {
            commissionService.deleteCommission(99L);
        });
        
        verify(commissionRepository, times(1)).existsById(99L);
        verify(commissionRepository, never()).deleteById(99L);
    }

    @Test
    void activateCommission() {
        commission.setActive(false);
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenAnswer(invocation -> {
            Commission saved = invocation.getArgument(0);
            assertTrue(saved.isActive());
            return saved;
        });
        
        CommissionDTO result = commissionService.activateCommission(1L);
        
        assertNotNull(result);
        assertTrue(result.isActive());
        verify(commissionRepository, times(1)).findById(1L);
        verify(commissionRepository, times(1)).save(any(Commission.class));
    }

    @Test
    void deactivateCommission() {
        commission.setActive(true);
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenAnswer(invocation -> {
            Commission saved = invocation.getArgument(0);
            assertFalse(saved.isActive());
            return saved;
        });
        
        CommissionDTO result = commissionService.deactivateCommission(1L);
        
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(commissionRepository, times(1)).findById(1L);
        verify(commissionRepository, times(1)).save(any(Commission.class));
    }
}
package com.orleansgo.commission.service;

import com.orleansgo.commission.model.Commission;
import com.orleansgo.commission.model.TauxCommission;
import com.orleansgo.commission.repository.CommissionRepository;
import com.orleansgo.commission.repository.TauxCommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommissionServiceTest {

    @Mock
    private CommissionRepository commissionRepository;
    
    @Mock
    private TauxCommissionRepository tauxCommissionRepository;

    @InjectMocks
    private CommissionService commissionService;

    private Commission commission;
    private TauxCommission tauxCommission;
    private UUID commissionId;
    private UUID tauxCommissionId;
    private UUID trajetId;
    private UUID conducteurId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        commissionId = UUID.randomUUID();
        tauxCommissionId = UUID.randomUUID();
        trajetId = UUID.randomUUID();
        conducteurId = UUID.randomUUID();
        
        tauxCommission = new TauxCommission();
        tauxCommission.setId(tauxCommissionId);
        tauxCommission.setTaux(new BigDecimal("0.15"));
        tauxCommission.setTypeVehicule("STANDARD");
        tauxCommission.setRegion("Orleans");
        tauxCommission.setActif(true);
        
        commission = new Commission();
        commission.setId(commissionId);
        commission.setMontant(new BigDecimal("10.00"));
        commission.setDateCreation(LocalDateTime.now());
        commission.setTrajetId(trajetId);
        commission.setConducteurId(conducteurId);
        commission.setTauxApplique(tauxCommission.getTaux());
    }

    @Test
    public void testTrouverToutesLesCommissions() {
        when(commissionRepository.findAll()).thenReturn(Arrays.asList(commission));
        
        List<Commission> result = commissionService.trouverToutesLesCommissions();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commissionRepository, times(1)).findAll();
    }

    @Test
    public void testTrouverCommissionParId() {
        when(commissionRepository.findById(commissionId)).thenReturn(Optional.of(commission));
        
        Optional<Commission> result = commissionService.trouverCommissionParId(commissionId);
        
        assertNotNull(result);
        assertEquals(commissionId, result.get().getId());
        verify(commissionRepository, times(1)).findById(commissionId);
    }

    @Test
    public void testEnregistrerCommission() {
        when(commissionRepository.save(any(Commission.class))).thenReturn(commission);
        
        Commission result = commissionService.enregistrerCommission(commission);
        
        assertNotNull(result);
        assertEquals(commissionId, result.getId());
        verify(commissionRepository, times(1)).save(commission);
    }

    @Test
    public void testCalculerCommission() {
        BigDecimal montantTrajet = new BigDecimal("100.00");
        when(tauxCommissionRepository.findActiveRateByVehicleTypeAndRegion("STANDARD", "Orleans"))
            .thenReturn(Optional.of(tauxCommission));
        
        BigDecimal result = commissionService.calculerCommission(montantTrajet, "STANDARD", "Orleans");
        
        assertNotNull(result);
        assertEquals(new BigDecimal("15.00"), result);  // 100 * 0.15 = 15
    }

    @Test
    public void testTrouverCommissionsParConducteur() {
        when(commissionRepository.findByConducteurId(conducteurId)).thenReturn(Arrays.asList(commission));
        
        List<Commission> result = commissionService.trouverCommissionsParConducteur(conducteurId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conducteurId, result.get(0).getConducteurId());
        verify(commissionRepository, times(1)).findByConducteurId(conducteurId);
    }

    @Test
    public void testTrouverCommissionsParTrajet() {
        when(commissionRepository.findByTrajetId(trajetId)).thenReturn(Optional.of(commission));
        
        Optional<Commission> result = commissionService.trouverCommissionParTrajet(trajetId);
        
        assertNotNull(result);
        assertEquals(trajetId, result.get().getTrajetId());
        verify(commissionRepository, times(1)).findByTrajetId(trajetId);
    }

    @Test
    public void testTrouverCommissionsParPeriode() {
        LocalDateTime debut = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);
        
        when(commissionRepository.findByDateCreationBetween(debut, fin)).thenReturn(Arrays.asList(commission));
        
        List<Commission> result = commissionService.trouverCommissionsParPeriode(debut, fin);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commissionRepository, times(1)).findByDateCreationBetween(debut, fin);
    }

    @Test
    public void testTrouverTauxCommissionActifs() {
        when(tauxCommissionRepository.findByActif(true)).thenReturn(Arrays.asList(tauxCommission));
        
        List<TauxCommission> result = commissionService.trouverTauxCommissionActifs();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tauxCommissionId, result.get(0).getId());
        verify(tauxCommissionRepository, times(1)).findByActif(true);
    }

    @Test
    public void testEnregistrerTauxCommission() {
        when(tauxCommissionRepository.save(any(TauxCommission.class))).thenReturn(tauxCommission);
        
        TauxCommission result = commissionService.enregistrerTauxCommission(tauxCommission);
        
        assertNotNull(result);
        assertEquals(tauxCommissionId, result.getId());
        verify(tauxCommissionRepository, times(1)).save(tauxCommission);
    }
}
