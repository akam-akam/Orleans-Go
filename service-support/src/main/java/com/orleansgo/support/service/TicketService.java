package com.orleansgo.support.service;

import com.orleansgo.support.dto.TicketDTO;
import com.orleansgo.support.exception.TicketException;
import com.orleansgo.support.mapper.TicketMapper;
import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketDTO creerTicket(TicketDTO ticketDTO) {
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }

    public List<TicketDTO> listerTousLesTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO trouverParId(UUID id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::toDTO)
                .orElseThrow(() -> new TicketException("Ticket non trouvé"));
    }

    public void supprimerTicket(UUID id) {
        ticketRepository.deleteById(id);
    }
}
