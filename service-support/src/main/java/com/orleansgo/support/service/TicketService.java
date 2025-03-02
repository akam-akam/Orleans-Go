
package com.orleansgo.support.service;

import com.orleansgo.support.dto.TicketDTO;
import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.model.TicketStatut;
import com.orleansgo.support.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé avec l'ID: " + id));
        return convertToDTO(ticket);
    }

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        ticket.setDateCreation(LocalDateTime.now());
        ticket.setStatut(TicketStatut.OUVERT);
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    @Transactional
    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé avec l'ID: " + id));
        
        ticket.setSujet(ticketDTO.getSujet());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriorite(ticketDTO.getPriorite());
        ticket.setAssignedTo(ticketDTO.getAssignedTo());
        
        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    @Transactional
    public TicketDTO updateTicketStatus(Long id, TicketStatut statut) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé avec l'ID: " + id));
        
        ticket.setStatut(statut);
        if (statut == TicketStatut.FERME || statut == TicketStatut.RESOLU) {
            ticket.setDateCloture(LocalDateTime.now());
        }
        
        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    public List<TicketDTO> getTicketsByUserId(String userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsByStatus(TicketStatut statut) {
        return ticketRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsByAssignedAgent(String agentId) {
        return ticketRepository.findByAssignedTo(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .sujet(ticket.getSujet())
                .description(ticket.getDescription())
                .userId(ticket.getUserId())
                .statut(ticket.getStatut())
                .priorite(ticket.getPriorite())
                .assignedTo(ticket.getAssignedTo())
                .dateCreation(ticket.getDateCreation())
                .dateModification(ticket.getDateModification())
                .dateCloture(ticket.getDateCloture())
                .nombreMessages(ticket.getMessages() != null ? ticket.getMessages().size() : 0)
                .build();
    }

    private Ticket convertToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setSujet(ticketDTO.getSujet());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setUserId(ticketDTO.getUserId());
        ticket.setPriorite(ticketDTO.getPriorite());
        return ticket;
    }
}
