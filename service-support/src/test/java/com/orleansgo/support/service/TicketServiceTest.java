
package com.orleansgo.support.service;

import com.orleansgo.support.dto.TicketDTO;
import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.model.TicketPriorite;
import com.orleansgo.support.model.TicketStatut;
import com.orleansgo.support.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private TicketDTO ticketDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSujet("Problème de paiement");
        ticket.setDescription("Je n'arrive pas à effectuer un paiement");
        ticket.setUserId("user123");
        ticket.setStatut(TicketStatut.OUVERT);
        ticket.setPriorite(TicketPriorite.NORMALE);
        ticket.setDateCreation(now);
        ticket.setDateModification(now);
        
        ticketDTO = TicketDTO.builder()
                .id(1L)
                .sujet("Problème de paiement")
                .description("Je n'arrive pas à effectuer un paiement")
                .userId("user123")
                .statut(TicketStatut.OUVERT)
                .priorite(TicketPriorite.NORMALE)
                .dateCreation(now)
                .dateModification(now)
                .nombreMessages(0)
                .build();
    }

    @Test
    void getAllTickets_shouldReturnAllTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));
        
        List<TicketDTO> result = ticketService.getAllTickets();
        
        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
    }

    @Test
    void getTicketById_shouldReturnTicket_whenTicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        
        TicketDTO result = ticketService.getTicketById(1L);
        
        assertEquals(ticket.getId(), result.getId());
    }

    @Test
    void getTicketById_shouldThrowException_whenTicketDoesNotExist() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> ticketService.getTicketById(1L));
    }

    @Test
    void createTicket_shouldCreateAndReturnTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        
        TicketDTO result = ticketService.createTicket(ticketDTO);
        
        assertEquals(ticket.getId(), result.getId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void updateTicket_shouldUpdateAndReturnTicket_whenTicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        
        TicketDTO result = ticketService.updateTicket(1L, ticketDTO);
        
        assertEquals(ticket.getId(), result.getId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void updateTicket_shouldThrowException_whenTicketDoesNotExist() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> ticketService.updateTicket(1L, ticketDTO));
    }

    @Test
    void updateTicketStatus_shouldUpdateStatus_whenTicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        
        TicketDTO result = ticketService.updateTicketStatus(1L, TicketStatut.RESOLU);
        
        assertEquals(TicketStatut.RESOLU, ticket.getStatut());
        assertNotNull(ticket.getDateCloture());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void getTicketsByUserId_shouldReturnUserTickets() {
        when(ticketRepository.findByUserId("user123")).thenReturn(Arrays.asList(ticket));
        
        List<TicketDTO> result = ticketService.getTicketsByUserId("user123");
        
        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
    }

    @Test
    void getTicketsByStatus_shouldReturnTicketsWithStatus() {
        when(ticketRepository.findByStatut(TicketStatut.OUVERT)).thenReturn(Arrays.asList(ticket));
        
        List<TicketDTO> result = ticketService.getTicketsByStatus(TicketStatut.OUVERT);
        
        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
    }

    @Test
    void getTicketsByAssignedAgent_shouldReturnAssignedTickets() {
        ticket.setAssignedTo("agent123");
        when(ticketRepository.findByAssignedTo("agent123")).thenReturn(Arrays.asList(ticket));
        
        List<TicketDTO> result = ticketService.getTicketsByAssignedAgent("agent123");
        
        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
    }
}
