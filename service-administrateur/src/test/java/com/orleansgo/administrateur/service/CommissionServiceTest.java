
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Commission;
import com.orleansgo.administrateur.repository.CommissionRepository;
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
    private Administrateur administrateur;

    @BeforeEach
    void setUp() {
        administrateur = new Administrateur();
        administrateur.setId(1L);
        administrateur.setNom("Dupont");
        administrateur.setPrenom("Jean");

        commission = new Commission();
        commission.setId(1L);
        commission.setTauxCommission(new BigDecimal("0.10"));
        commission.setDateDebut(LocalDateTime.now().minusDays(10));
        commission.setDescription("Commission initiale");
        commission.setModifiePar(administrateur);
    }

    @Test
    void findAllCommissions_shouldReturnAllCommissions() {
        // Given
        List<Commission> expectedCommissions = Arrays.asList(commission);
        when(commissionRepository.findAll()).thenReturn(expectedCommissions);

        // When
        List<Commission> actualCommissions = commissionService.findAllCommissions();

        // Then
        assertEquals(expectedCommissions, actualCommissions);
        verify(commissionRepository).findAll();
    }

    @Test
    void findCommissionById_whenCommissionExists_shouldReturnCommission() {
        // Given
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));

        // When
        Optional<Commission> result = commissionService.findCommissionById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(commission, result.get());
        verify(commissionRepository).findById(1L);
    }

    @Test
    void findCommissionById_whenCommissionDoesNotExist_shouldReturnEmpty() {
        // Given
        when(commissionRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Commission> result = commissionService.findCommissionById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(commissionRepository).findById(1L);
    }

    @Test
    void createCommission_shouldSaveCommission() {
        // Given
        when(commissionRepository.save(any(Commission.class))).thenReturn(commission);

        // When
        Commission result = commissionService.createCommission(commission, administrateur);

        // Then
        assertEquals(commission, result);
        assertEquals(administrateur, commission.getModifiePar());
        verify(commissionRepository).save(commission);
    }

    @Test
    void updateCommission_whenCommissionExistsAndNotClosed_shouldUpdateCommission() {
        // Given
        Commission updatedCommission = new Commission();
        updatedCommission.setTauxCommission(new BigDecimal("0.15"));
        updatedCommission.setDescription("Commission mise à jour");
        
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Commission> result = commissionService.updateCommission(1L, updatedCommission, administrateur);

        // Then
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("0.15"), result.get().getTauxCommission());
        assertEquals("Commission mise à jour", result.get().getDescription());
        assertEquals(administrateur, result.get().getModifiePar());
        assertNotNull(result.get().getDateModification());
        verify(commissionRepository).findById(1L);
        verify(commissionRepository).save(commission);
    }

    @Test
    void updateCommission_whenCommissionIsAlreadyClosed_shouldThrowRuntimeException() {
        // Given
        commission.setDateFin(LocalDateTime.now().minusDays(1));
        Commission updatedCommission = new Commission();
        
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));

        // When & Then
        assertThrows(RuntimeException.class, () -> commissionService.updateCommission(1L, updatedCommission, administrateur));
        verify(commissionRepository).findById(1L);
        verify(commissionRepository, never()).save(any(Commission.class));
    }

    @Test
    void clotureCommission_whenCommissionExistsAndNotClosed_shouldCloseCommission() {
        // Given
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Commission> result = commissionService.clotureCommission(1L, administrateur);

        // Then
        assertTrue(result.isPresent());
        assertNotNull(result.get().getDateFin());
        assertEquals(administrateur, result.get().getModifiePar());
        assertNotNull(result.get().getDateModification());
        verify(commissionRepository).findById(1L);
        verify(commissionRepository).save(commission);
    }

    @Test
    void clotureCommission_whenCommissionIsAlreadyClosed_shouldThrowRuntimeException() {
        // Given
        commission.setDateFin(LocalDateTime.now().minusDays(1));
        when(commissionRepository.findById(1L)).thenReturn(Optional.of(commission));

        // When & Then
        assertThrows(RuntimeException.class, () -> commissionService.clotureCommission(1L, administrateur));
        verify(commissionRepository).findById(1L);
        verify(commissionRepository, never()).save(any(Commission.class));
    }

    @Test
    void ajusterCommission_whenCurrentCommissionExists_shouldCloseItAndCreateNew() {
        // Given
        BigDecimal nouveauTaux = new BigDecimal("0.20");
        Commission nouvelleCommission = new Commission();
        nouvelleCommission.setId(2L);
        
        when(commissionRepository.findCurrentCommission()).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenReturn(nouvelleCommission);

        // When
        Commission result = commissionService.ajusterCommission(nouveauTaux, administrateur);

        // Then
        assertEquals(nouvelleCommission, result);
        verify(commissionRepository).findCurrentCommission();
        verify(commissionRepository, times(2)).save(any(Commission.class));
    }

    @Test
    void ajusterCommission_whenNoCurrentCommission_shouldCreateNew() {
        // Given
        BigDecimal nouveauTaux = new BigDecimal("0.20");
        Commission nouvelleCommission = new Commission();
        nouvelleCommission.setId(2L);
        
        when(commissionRepository.findCurrentCommission()).thenReturn(Optional.empty());
        when(commissionRepository.save(any(Commission.class))).thenReturn(nouvelleCommission);

        // When
        Commission result = commissionService.ajusterCommission(nouveauTaux, administrateur);

        // Then
        assertEquals(nouvelleCommission, result);
        verify(commissionRepository).findCurrentCommission();
        verify(commissionRepository).save(any(Commission.class));
    }
}
