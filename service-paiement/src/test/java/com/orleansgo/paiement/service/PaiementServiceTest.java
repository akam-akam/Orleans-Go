
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
