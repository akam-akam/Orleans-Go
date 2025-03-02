
package com.orleansgo.support.controller;

import com.orleansgo.support.dto.TicketDTO;
import com.orleansgo.support.model.TicketStatut;
import com.orleansgo.support.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.createTicket(ticketDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDTO));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TicketDTO> updateTicketStatus(@PathVariable Long id, @RequestParam TicketStatut statut) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, statut));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<TicketDTO>> getTicketsByStatus(@PathVariable TicketStatut statut) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(statut));
    }

    @GetMapping("/assigned/{agentId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByAssignedAgent(@PathVariable String agentId) {
        return ResponseEntity.ok(ticketService.getTicketsByAssignedAgent(agentId));
    }
}
