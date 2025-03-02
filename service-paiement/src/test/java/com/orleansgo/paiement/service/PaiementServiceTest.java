
package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.dto.RemboursementDTO;
import com.orleansgo.paiement.dto.TransactionDTO;
import com.orleansgo.paiement.model.MethodePaiement;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.model.StatutPaiement;
import com.orleansgo.paiement.model.Transaction;
import com.orleansgo.paiement.repository.PaiementRepository;
import com.orleansgo.paiement.repository.TransactionRepository;
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
class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;
    
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaiementService paiementService;

    private Paiement paiement;
    private PaiementDTO paiementDTO;
    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private RemboursementDTO remboursementDTO;

    @BeforeEach
    void setUp() {
        paiement = new Paiement();
        paiement.setId(1L);
        paiement.setTrajetId(100L);
        paiement.setUtilisateurId(10L);
        paiement.setConducteurId(20L);
        paiement.setMontant(new BigDecimal("25.50"));
        paiement.setMethode(MethodePaiement.CARTE);
        paiement.setStatut(StatutPaiement.COMPLETE);
        paiement.setReference("PAY-123456");
        paiement.setDateCreation(LocalDateTime.now());
        
        paiementDTO = new PaiementDTO();
        paiementDTO.setId(1L);
        paiementDTO.setTrajetId(100L);
        paiementDTO.setUtilisateurId(10L);
        paiementDTO.setConducteurId(20L);
        paiementDTO.setMontant(new BigDecimal("25.50"));
        paiementDTO.setMethode(MethodePaiement.CARTE.name());
        paiementDTO.setStatut(StatutPaiement.COMPLETE.name());
        paiementDTO.setReference("PAY-123456");
        paiementDTO.setDateCreation(LocalDateTime.now());
        
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setPaiementId(1L);
        transaction.setMontant(new BigDecimal("25.50"));
        transaction.setType("PAIEMENT");
        transaction.setStatut("SUCCES");
        transaction.setReference("TRX-123456");
        transaction.setDateCreation(LocalDateTime.now());
        
        transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setPaiementId(1L);
        transactionDTO.setMontant(new BigDecimal("25.50"));
        transactionDTO.setType("PAIEMENT");
        transactionDTO.setStatut("SUCCES");
        transactionDTO.setReference("TRX-123456");
        transactionDTO.setDateCreation(LocalDateTime.now());
        
        remboursementDTO = new RemboursementDTO();
        remboursementDTO.setPaiementId(1L);
        remboursementDTO.setMontant(new BigDecimal("25.50"));
        remboursementDTO.setRaison("Annulation du trajet");
    }

    @Test
    void getAllPaiements() {
        when(paiementRepository.findAll()).thenReturn(Arrays.asList(paiement));
        
        List<PaiementDTO> result = paiementService.getAllPaiements();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paiement.getId(), result.get(0).getId());
    }

    @Test
    void getPaiementById() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        
        PaiementDTO result = paiementService.getPaiementById(1L);
        
        assertNotNull(result);
        assertEquals(paiement.getId(), result.getId());
    }
    
    @Test
    void getPaiementsByUtilisateurId() {
        when(paiementRepository.findByUtilisateurId(10L)).thenReturn(Arrays.asList(paiement));
        
        List<PaiementDTO> result = paiementService.getPaiementsByUtilisateurId(10L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paiement.getUtilisateurId(), result.get(0).getUtilisateurId());
    }
    
    @Test
    void createPaiement() {
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        PaiementDTO result = paiementService.createPaiement(paiementDTO);
        
        assertNotNull(result);
        assertEquals(paiement.getId(), result.getId());
        verify(paiementRepository, times(1)).save(any(Paiement.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void processerRemboursement() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        PaiementDTO result = paiementService.processerRemboursement(remboursementDTO);
        
        assertNotNull(result);
        assertEquals(StatutPaiement.REMBOURSE.name(), result.getStatut());
        verify(paiementRepository, times(1)).save(any(Paiement.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void getTransactionsByPaiementId() {
        when(transactionRepository.findByPaiementId(1L)).thenReturn(Arrays.asList(transaction));
        
        List<TransactionDTO> result = paiementService.getTransactionsByPaiementId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction.getPaiementId(), result.get(0).getPaiementId());
    }
}
package com.orleansgo.paiement.service;

import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.model.StatusPaiement;
import com.orleansgo.paiement.repository.PaiementRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @InjectMocks
    private PaiementService paiementService;

    private Paiement paiement;

    @BeforeEach
    void setUp() {
        paiement = new Paiement();
        paiement.setId(1L);
        paiement.setTrajetId(100L);
        paiement.setUtilisateurId(200L);
        paiement.setConducteurId(300L);
        paiement.setMontant(new BigDecimal("25.50"));
        paiement.setStatus(StatusPaiement.EN_ATTENTE);
        paiement.setDateCreation(LocalDateTime.now());
        paiement.setMethodePaiement("CARTE");
        paiement.setReference("PAY-REF-12345");
    }

    @Test
    void shouldSavePaiement() {
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);

        Paiement savedPaiement = paiementService.savePaiement(paiement);

        assertThat(savedPaiement).isNotNull();
        assertThat(savedPaiement.getId()).isEqualTo(1L);
        assertThat(savedPaiement.getMontant()).isEqualTo(new BigDecimal("25.50"));
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void shouldGetAllPaiements() {
        when(paiementRepository.findAll()).thenReturn(Arrays.asList(paiement));

        List<Paiement> paiements = paiementService.getAllPaiements();

        assertThat(paiements).isNotEmpty();
        assertThat(paiements).hasSize(1);
        assertThat(paiements.get(0).getMontant()).isEqualTo(new BigDecimal("25.50"));
        verify(paiementRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPaiementById() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));

        Optional<Paiement> foundPaiement = paiementService.getPaiementById(1L);

        assertThat(foundPaiement).isPresent();
        assertThat(foundPaiement.get().getId()).isEqualTo(1L);
        verify(paiementRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetPaiementsByUtilisateurId() {
        when(paiementRepository.findByUtilisateurId(200L)).thenReturn(Arrays.asList(paiement));

        List<Paiement> paiements = paiementService.getPaiementsByUtilisateurId(200L);

        assertThat(paiements).isNotEmpty();
        assertThat(paiements).hasSize(1);
        assertThat(paiements.get(0).getUtilisateurId()).isEqualTo(200L);
        verify(paiementRepository, times(1)).findByUtilisateurId(200L);
    }

    @Test
    void shouldGetPaiementsByConducteurId() {
        when(paiementRepository.findByConducteurId(300L)).thenReturn(Arrays.asList(paiement));

        List<Paiement> paiements = paiementService.getPaiementsByConducteurId(300L);

        assertThat(paiements).isNotEmpty();
        assertThat(paiements).hasSize(1);
        assertThat(paiements.get(0).getConducteurId()).isEqualTo(300L);
        verify(paiementRepository, times(1)).findByConducteurId(300L);
    }

    @Test
    void shouldGetPaiementsByTrajetId() {
        when(paiementRepository.findByTrajetId(100L)).thenReturn(Arrays.asList(paiement));

        List<Paiement> paiements = paiementService.getPaiementsByTrajetId(100L);

        assertThat(paiements).isNotEmpty();
        assertThat(paiements).hasSize(1);
        assertThat(paiements.get(0).getTrajetId()).isEqualTo(100L);
        verify(paiementRepository, times(1)).findByTrajetId(100L);
    }

    @Test
    void shouldUpdatePaiementStatus() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);

        Optional<Paiement> updatedPaiement = paiementService.updatePaiementStatus(1L, StatusPaiement.COMPLETE);

        assertThat(updatedPaiement).isPresent();
        assertThat(updatedPaiement.get().getStatus()).isEqualTo(StatusPaiement.COMPLETE);
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void shouldProcessRemboursement() {
        paiement.setStatus(StatusPaiement.COMPLETE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);

        Optional<Paiement> remboursePaiement = paiementService.processRemboursement(1L, "Remboursement client insatisfait");

        assertThat(remboursePaiement).isPresent();
        assertThat(remboursePaiement.get().getStatus()).isEqualTo(StatusPaiement.REMBOURSE);
        assertThat(remboursePaiement.get().getCommentaire()).isEqualTo("Remboursement client insatisfait");
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void shouldDeletePaiement() {
        doNothing().when(paiementRepository).deleteById(1L);

        paiementService.deletePaiement(1L);

        verify(paiementRepository, times(1)).deleteById(1L);
    }
}
package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.model.StatutPaiement;
import com.orleansgo.paiement.repository.PaiementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @InjectMocks
    private PaiementService paiementService;

    private Paiement paiement;
    private PaiementDTO paiementDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paiement = new Paiement();
        paiement.setId(1L);
        paiement.setUtilisateurId(100L);
        paiement.setTrajetId(200L);
        paiement.setMontant(15.50);
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        paiement.setDateCreation(LocalDateTime.now());
        paiement.setMethodePaiement("CARTE");
        paiement.setReference("PAY-REF-12345");

        paiementDTO = new PaiementDTO();
        paiementDTO.setId(1L);
        paiementDTO.setUtilisateurId(100L);
        paiementDTO.setTrajetId(200L);
        paiementDTO.setMontant(15.50);
        paiementDTO.setStatut(StatutPaiement.EN_ATTENTE);
        paiementDTO.setDateCreation(LocalDateTime.now());
        paiementDTO.setMethodePaiement("CARTE");
        paiementDTO.setReference("PAY-REF-12345");
    }

    @Test
    void testFindAll() {
        when(paiementRepository.findAll()).thenReturn(Arrays.asList(paiement));
        
        List<PaiementDTO> result = paiementService.findAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paiementDTO.getId(), result.get(0).getId());
        verify(paiementRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        
        PaiementDTO result = paiementService.findById(1L);
        
        assertNotNull(result);
        assertEquals(paiementDTO.getId(), result.getId());
        verify(paiementRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByUtilisateurId() {
        when(paiementRepository.findByUtilisateurId(100L)).thenReturn(Arrays.asList(paiement));
        
        List<PaiementDTO> result = paiementService.findByUtilisateurId(100L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paiementDTO.getId(), result.get(0).getId());
        verify(paiementRepository, times(1)).findByUtilisateurId(100L);
    }

    @Test
    void testFindByTrajetId() {
        when(paiementRepository.findByTrajetId(200L)).thenReturn(Arrays.asList(paiement));
        
        List<PaiementDTO> result = paiementService.findByTrajetId(200L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paiementDTO.getId(), result.get(0).getId());
        verify(paiementRepository, times(1)).findByTrajetId(200L);
    }

    @Test
    void testCreate() {
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        
        PaiementDTO result = paiementService.create(paiementDTO);
        
        assertNotNull(result);
        assertEquals(paiementDTO.getId(), result.getId());
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void testUpdate() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        
        PaiementDTO updated = new PaiementDTO();
        updated.setId(1L);
        updated.setMontant(20.0);
        
        PaiementDTO result = paiementService.update(1L, updated);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void testConfirmerPaiement() {
        paiement.setStatut(StatutPaiement.COMPLETE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        
        PaiementDTO result = paiementService.confirmerPaiement(1L);
        
        assertNotNull(result);
        assertEquals(StatutPaiement.COMPLETE, result.getStatut());
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void testAnnulerPaiement() {
        paiement.setStatut(StatutPaiement.ANNULE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        
        PaiementDTO result = paiementService.annulerPaiement(1L);
        
        assertNotNull(result);
        assertEquals(StatutPaiement.ANNULE, result.getStatut());
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void testRembourserPaiement() {
        paiement.setStatut(StatutPaiement.REMBOURSE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);
        
        PaiementDTO result = paiementService.rembourserPaiement(1L);
        
        assertNotNull(result);
        assertEquals(StatutPaiement.REMBOURSE, result.getStatut());
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void testDelete() {
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        doNothing().when(paiementRepository).deleteById(1L);
        
        paiementService.delete(1L);
        
        verify(paiementRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).deleteById(1L);
    }
}
