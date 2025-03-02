
package com.orleansgo.support.service;

import com.orleansgo.support.model.FAQ;
import com.orleansgo.support.model.StatutTicket;
import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.model.TicketMessage;
import com.orleansgo.support.repository.FAQRepository;
import com.orleansgo.support.repository.TicketMessageRepository;
import com.orleansgo.support.repository.TicketRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SupportServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    
    @Mock
    private TicketMessageRepository ticketMessageRepository;
    
    @Mock
    private FAQRepository faqRepository;

    @InjectMocks
    private SupportService supportService;

    private Ticket ticket;
    private TicketMessage ticketMessage;
    private FAQ faq;
    private UUID ticketId;
    private UUID messageId;
    private UUID faqId;
    private UUID utilisateurId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        ticketId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        faqId = UUID.randomUUID();
        utilisateurId = UUID.randomUUID();
        
        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setUtilisateurId(utilisateurId);
        ticket.setSujet("Problème de paiement");
        ticket.setDescription("Je n'arrive pas à effectuer un paiement");
        ticket.setStatut(StatutTicket.OUVERT);
        ticket.setDateCreation(LocalDateTime.now());
        
        ticketMessage = new TicketMessage();
        ticketMessage.setId(messageId);
        ticketMessage.setTicketId(ticketId);
        ticketMessage.setUtilisateurId(utilisateurId);
        ticketMessage.setMessage("Message de test");
        ticketMessage.setDateCreation(LocalDateTime.now());
        
        faq = new FAQ();
        faq.setId(faqId);
        faq.setQuestion("Comment fonctionne le paiement ?");
        faq.setReponse("Le paiement est géré par notre système sécurisé.");
        faq.setCategorie("Paiement");
        faq.setActif(true);
    }

    @Test
    public void testTrouverTousLesTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));
        
        List<Ticket> result = supportService.trouverTousLesTickets();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    public void testTrouverTicketParId() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        
        Optional<Ticket> result = supportService.trouverTicketParId(ticketId);
        
        assertNotNull(result);
        assertEquals(ticketId, result.get().getId());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    public void testEnregistrerTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        
        Ticket result = supportService.enregistrerTicket(ticket);
        
        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testChangerStatutTicket() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        
        Ticket updatedTicket = supportService.changerStatutTicket(ticketId, StatutTicket.EN_COURS);
        
        assertNotNull(updatedTicket);
        assertEquals(StatutTicket.EN_COURS, updatedTicket.getStatut());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testTrouverTicketsParUtilisateur() {
        when(ticketRepository.findByUtilisateurId(utilisateurId)).thenReturn(Arrays.asList(ticket));
        
        List<Ticket> result = supportService.trouverTicketsParUtilisateur(utilisateurId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(utilisateurId, result.get(0).getUtilisateurId());
        verify(ticketRepository, times(1)).findByUtilisateurId(utilisateurId);
    }

    @Test
    public void testTrouverTicketsParStatut() {
        when(ticketRepository.findByStatut(StatutTicket.OUVERT)).thenReturn(Arrays.asList(ticket));
        
        List<Ticket> result = supportService.trouverTicketsParStatut(StatutTicket.OUVERT);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(StatutTicket.OUVERT, result.get(0).getStatut());
        verify(ticketRepository, times(1)).findByStatut(StatutTicket.OUVERT);
    }

    @Test
    public void testAjouterMessageAuTicket() {
        when(ticketMessageRepository.save(any(TicketMessage.class))).thenReturn(ticketMessage);
        
        TicketMessage result = supportService.ajouterMessageAuTicket(ticketMessage);
        
        assertNotNull(result);
        assertEquals(messageId, result.getId());
        verify(ticketMessageRepository, times(1)).save(ticketMessage);
    }

    @Test
    public void testTrouverMessagesParTicket() {
        when(ticketMessageRepository.findByTicketId(ticketId)).thenReturn(Arrays.asList(ticketMessage));
        
        List<TicketMessage> result = supportService.trouverMessagesParTicket(ticketId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ticketId, result.get(0).getTicketId());
        verify(ticketMessageRepository, times(1)).findByTicketId(ticketId);
    }

    @Test
    public void testTrouverToutesLesFAQs() {
        when(faqRepository.findByActif(true)).thenReturn(Arrays.asList(faq));
        
        List<FAQ> result = supportService.trouverToutesLesFAQsActives();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faqRepository, times(1)).findByActif(true);
    }

    @Test
    public void testTrouverFAQParId() {
        when(faqRepository.findById(faqId)).thenReturn(Optional.of(faq));
        
        Optional<FAQ> result = supportService.trouverFAQParId(faqId);
        
        assertNotNull(result);
        assertEquals(faqId, result.get().getId());
        verify(faqRepository, times(1)).findById(faqId);
    }

    @Test
    public void testEnregistrerFAQ() {
        when(faqRepository.save(any(FAQ.class))).thenReturn(faq);
        
        FAQ result = supportService.enregistrerFAQ(faq);
        
        assertNotNull(result);
        assertEquals(faqId, result.getId());
        verify(faqRepository, times(1)).save(faq);
    }

    @Test
    public void testTrouverFAQsParCategorie() {
        when(faqRepository.findByCategorie("Paiement")).thenReturn(Arrays.asList(faq));
        
        List<FAQ> result = supportService.trouverFAQsParCategorie("Paiement");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paiement", result.get(0).getCategorie());
        verify(faqRepository, times(1)).findByCategorie("Paiement");
    }
}
