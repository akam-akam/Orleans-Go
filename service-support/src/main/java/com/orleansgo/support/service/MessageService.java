
package com.orleansgo.support.service;

import com.orleansgo.support.dto.MessageDTO;
import com.orleansgo.support.model.Message;
import com.orleansgo.support.model.Ticket;
import com.orleansgo.support.repository.MessageRepository;
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
public class MessageService {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;

    public List<MessageDTO> getMessagesByTicketId(Long ticketId) {
        return messageRepository.findByTicketId(ticketId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO addMessage(Long ticketId, MessageDTO messageDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trouvé avec l'ID: " + ticketId));
        
        Message message = new Message();
        message.setContenu(messageDTO.getContenu());
        message.setAuteurId(messageDTO.getAuteurId());
        message.setAuteurNom(messageDTO.getAuteurNom());
        message.setEstAgent(messageDTO.getEstAgent());
        message.setTicket(ticket);
        message.setDateCreation(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        
        return convertToDTO(savedMessage);
    }

    private MessageDTO convertToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .contenu(message.getContenu())
                .auteurId(message.getAuteurId())
                .auteurNom(message.getAuteurNom())
                .estAgent(message.getEstAgent())
                .ticketId(message.getTicket().getId())
                .dateCreation(message.getDateCreation())
                .build();
    }
}
